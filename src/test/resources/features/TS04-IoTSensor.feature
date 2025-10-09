Feature: API de datos de sensores IoT
  Como developer
  Quiero implementar endpoints para recibir y procesar datos de sensores IoT
  Para mantener información actualizada de contenedores en tiempo real

  Background:
    Given el sistema expone el endpoint "POST /api/v1/sensor-readings"
    And existe el contenedor con id "CONTAINER-001"
    And el cliente está autenticado con un token válido

  Scenario: Recepción y almacenamiento exitoso de lectura de sensor
    When envía una lectura con:
      | containerId   | fillLevel | temperature | batteryLevel | recordedAt               |
      | CONTAINER-001 | 45        | 25          | 80           | 2025-10-09T01:00:00Z     |
    And la petición incluye encabezados:
      | Content-Type  | application/json |
      | Authorization | Bearer <token>   |
    Then la respuesta debe tener código 201
    And se almacena una lectura con estado de validación "VALID"
    And el contenedor "CONTAINER-001" debe actualizar su nivel actual a 45
    And se publica un evento de actualización en tiempo real para el contenedor "CONTAINER-001"

  Scenario: Validación exitosa cuando todos los valores están en rango
    Given existe una lectura recibida para el contenedor "CONTAINER-001" con fillLevel 45, temperature 25 y batteryLevel 80
    When el sistema valida la lectura
    Then la lectura debe marcarse como validada
    And el estado de validación debe ser "VALID"
    And no debe marcarse como anomalía
    And no debe marcarse como error de sensor

  Scenario: Anomalía cuando la batería está por debajo de 10%
    When envía una lectura con:
      | containerId   | fillLevel | temperature | batteryLevel |
      | CONTAINER-001 | 45        | 25          | 5            |
    Then la respuesta debe tener código 201
    And la lectura debe tener estado de validación "ANOMALY"
    And la lectura debe requerir mantenimiento
    And se genera una alerta de mantenimiento para el contenedor "CONTAINER-001"

  Scenario: Error de sensor cuando el nivel de llenado es inválido
    When envía una lectura con:
      | containerId   | fillLevel | temperature | batteryLevel |
      | CONTAINER-001 | 120       | 25          | 80           |
    Then la respuesta debe tener código 400
    And el cuerpo de respuesta debe contener el mensaje "Percentage must be between 0 and 100"

  Scenario Outline: Autenticación requerida para registrar lecturas
    When envía una lectura con autenticación "<auth>"
    Then la respuesta debe tener código <status>

    Examples:
      | auth     | status |
      | inválida | 401    |
      | ausente  | 401    |

  Scenario: Actualización del estado del contenedor en tiempo real
    Given existe una lectura válida con fillLevel 85 para "CONTAINER-001"
    When el sistema procesa la lectura
    Then el contenedor "CONTAINER-001" debe reflejar el nuevo nivel y estado actualizados en tiempo real
