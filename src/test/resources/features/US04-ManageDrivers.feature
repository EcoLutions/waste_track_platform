Feature: Gestión de conductores por administrador
  Como administrador municipal
  Quiero crear y gestionar cuentas de conductores de mi distrito
  Para que puedan acceder a la aplicación móvil de rutas

  Background:
    Given el administrador está autenticado

  Scenario: Creación exitosa de cuenta de conductor
    When crea una cuenta de conductor con los siguientes datos:
      | districtId  | fullName      | documentNumber | phoneNumber   | userId   | driverLicense | expiryDate   | emailAddress            |
      | 150101      | John Doe      | 12345678       | +51987654321  | USR-001  | A1234567      | 2026-01-01   | john.doe@example.com    |
    Then el sistema genera credenciales para el conductor
    And el estado inicial del conductor es "AVAILABLE"

  Scenario: Inicio exitoso de ruta
    Given existe un conductor con estado "AVAILABLE"
    When inicia una ruta
    Then el estado del conductor debe ser "ON_ROUTE"

  Scenario: Finalización exitosa de ruta
    Given el conductor tiene una ruta activa y ha trabajado 10 horas
    When completa la ruta con duración de 5 horas
    Then el estado del conductor debe ser "AVAILABLE"
    And el total de horas trabajadas debe ser 15

  Scenario: Suspensión de conductor
    Given el conductor está registrado
    When el administrador suspende al conductor con el motivo "Driver violated safety protocol"
    Then el estado del conductor debe ser "SUSPENDED"

  Scenario: Asignación y desasignación de vehículo
    Given el conductor está registrado
    When el administrador asigna el vehículo con id "VEH-001"
    Then el vehículo asignado debe ser "VEH-001"
    When el administrador desasigna el vehículo
    Then el conductor no debe tener vehículo asignado
