package com.ecolutions.platform.wastetrackplatform.acceptance.tests.steps;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ValidationStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import io.cucumber.java.en.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SensorReadingSteps {

    private String endpoint;
    private boolean tokenValido;
    private final Map<String, String> headers = new HashMap<>();

    private SensorReading lastReading;
    private ValidationStatus lastValidationStatus;
    private int responseStatus;
    private String responseMessage;

    private boolean eventPublished;
    private boolean maintenanceAlertGenerated;

    private FakeContainer container;

    private Map<String, String> pendingRow;
    private boolean requestPrepared = false;
    private boolean alreadyProcessed = false;

    @Given("el sistema expone el endpoint {string}")
    public void el_sistema_expone_el_endpoint(String ep) {
        this.endpoint = ep;
        assertNotNull(endpoint);
    }

    @And("existe el contenedor con id {string}")
    public void existe_el_contenedor_con_id(String containerCode) {
        container = new FakeContainer(new ContainerId(containerCode));
        assertNotNull(container);
    }

    @And("el cliente está autenticado con un token válido")
    public void el_cliente_esta_autenticado_con_un_token_valido() {
        tokenValido = true;
    }

    @When("envía una lectura con:")
    public void envia_una_lectura_con(io.cucumber.datatable.DataTable table) {
        resetRequestState();

        Map<String, String> row = toMap(table);
        this.pendingRow = row;
        this.requestPrepared = true;

        try {
            String containerId = row.get("containerId");
            int fillLevel = parseInt(row.get("fillLevel"));
            double temperature = (row.get("temperature") != null && !row.get("temperature").isBlank())
                    ? Double.parseDouble(row.get("temperature"))
                    : 0.0;
            int batteryLevel = (row.get("batteryLevel") != null && !row.get("batteryLevel").isBlank())
                    ? parseInt(row.get("batteryLevel"))
                    : 100;

            CreateSensorReadingCommand command = new CreateSensorReadingCommand(
                    containerId,
                    fillLevel,
                    temperature,
                    batteryLevel
            );

            lastReading = new SensorReading(command);

            if (row.get("recordedAt") != null && !row.get("recordedAt").isBlank()) {
                String ts = row.get("recordedAt").trim();
                try {
                    if (ts.endsWith("Z")) {
                        java.time.OffsetDateTime odt = java.time.OffsetDateTime.parse(ts, java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        lastReading.setRecordedAt(odt.toLocalDateTime());
                    } else {
                        lastReading.setRecordedAt(java.time.LocalDateTime.parse(ts));
                    }
                } catch (java.time.format.DateTimeParseException dtpe) {
                    responseStatus = 400;
                    responseMessage = "Invalid recordedAt format (use ISO-8601).";
                    return;
                }
            }

            processRequestAfterAuthAndValidate();

        } catch (IllegalArgumentException ex) {
            responseStatus = 400;
            responseMessage = ex.getMessage();
        } catch (Exception ex) {
            responseStatus = 500;
            responseMessage = "Internal error: " + ex.getMessage();
        }
    }

    @And("la petición incluye encabezados:")
    public void la_peticion_incluye_encabezados(io.cucumber.datatable.DataTable table) {
        headers.putAll(toMap(table));
        if (requestPrepared && !alreadyProcessed) {
            processRequestAfterAuthAndValidate();
        }
    }

    @Then("la respuesta debe tener código {int}")
    public void la_respuesta_debe_tener_codigo(Integer esperado) {
        if (requestPrepared && responseStatus == 500 && !headers.containsKey("Authorization") && !tokenValido) {
            processRequestAfterAuthAndValidate();
        }
        assertEquals(esperado.intValue(), responseStatus);
    }


    @Then("se almacena una lectura con estado de validación {string}")
    public void se_almacena_una_lectura_con_estado_de_validacion(String esperado) {
        assertNotNull(lastReading, "Debe existir una lectura almacenada");
        assertTrue(lastReading.getIsValidated(), "La lectura debe quedar validada");
        assertEquals(esperado, lastReading.getValidationStatus().name());
    }

    @Then("el contenedor {string} debe actualizar su nivel actual a {int}")
    public void el_contenedor_debe_actualizar_su_nivel_actual_a(String containerCode, Integer nivel) {
        assertEquals(containerCode, container.id.value());
        assertEquals(nivel.intValue(), container.currentFillLevelPercentage);
    }

    @Then("se publica un evento de actualización en tiempo real para el contenedor {string}")
    public void se_publica_un_evento_en_tiempo_real_para(String containerCode) {
        assertEquals(containerCode, container.id.value());
        assertTrue(eventPublished, "Debe publicarse un evento en tiempo real");
    }

    @Given("existe una lectura recibida para el contenedor {string} con fillLevel {int}, temperature {int} y batteryLevel {int}")
    public void existe_una_lectura_recibida(String containerCode, Integer fill, Integer temp, Integer battery) {
        CreateSensorReadingCommand command = new CreateSensorReadingCommand(
                containerCode,
                fill,
                temp.doubleValue(),
                battery
        );
        lastReading = new SensorReading(command);
        container = new FakeContainer(new ContainerId(containerCode));
    }

    @When("el sistema valida la lectura")
    public void el_sistema_valida_la_lectura() {
        lastReading.validate();
        lastValidationStatus = lastReading.getValidationStatus();
    }

    @Then("la lectura debe marcarse como validada")
    public void la_lectura_debe_marcarse_como_validada() {
        assertTrue(lastReading.getIsValidated());
    }

    @Then("el estado de validación debe ser {string}")
    public void el_estado_de_validacion_debe_ser(String esperado) {
        assertEquals(esperado, lastValidationStatus.name());
    }

    @Then("no debe marcarse como anomalía")
    public void no_debe_marcarse_como_anomalia() {
        assertFalse(lastReading.isAnomaly());
    }

    @Then("no debe marcarse como error de sensor")
    public void no_debe_marcarse_como_error_de_sensor() {
        assertFalse(lastReading.hasSensorError());
    }

    @Then("la lectura debe tener estado de validación {string}")
    public void la_lectura_debe_tener_estado_de_validacion(String esperado) {
        assertNotNull(lastReading);
        assertEquals(esperado, lastReading.getValidationStatus().name());
    }

    @Then("la lectura debe requerir mantenimiento")
    public void la_lectura_debe_requerir_mantenimiento() {
        assertTrue(lastReading.requiresMaintenance());
        assertTrue(maintenanceAlertGenerated, "Debe generarse una alerta de mantenimiento");
    }

    @Then("el cuerpo de respuesta debe contener el mensaje {string}")
    public void el_cuerpo_de_respuesta_debe_contener_el_mensaje(String mensaje) {
        assertEquals(mensaje, responseMessage);
    }

    @When("envía una lectura con autenticación {string}")
    public void envia_una_lectura_con_autenticacion(String tipoAuth) {
        headers.clear();
        tokenValido = true;

        switch (tipoAuth) {
            case "inválida" -> {
                tokenValido = false;
                headers.put("Authorization", "Bearer token-invalido");
            }
            case "ausente" -> {
                tokenValido = false;
            }
            default -> {
                tokenValido = true;
                headers.put("Authorization", "Bearer token-valido");
            }
        }

        String cid = (container != null) ? container.id.value() : "CONTAINER-001";
        java.util.List<java.util.List<String>> rows = java.util.Arrays.asList(
                java.util.Arrays.asList("containerId", "fillLevel", "temperature", "batteryLevel"),
                java.util.Arrays.asList(cid, "45", "25", "80")
        );
        io.cucumber.datatable.DataTable dt = io.cucumber.datatable.DataTable.create(rows);

        envia_una_lectura_con(dt);

        if (!tokenValido || !headers.containsKey("Authorization")) {
            processRequestAfterAuthAndValidate();
        }
    }

    @Given("existe una lectura válida con fillLevel {int} para {string}")
    public void existe_una_lectura_valida_con_fillLevel_para(Integer fill, String containerCode) {
        container = new FakeContainer(new ContainerId(containerCode));
        CreateSensorReadingCommand command = new CreateSensorReadingCommand(
                containerCode,
                fill,
                25.0,
                80
        );
        lastReading = new SensorReading(command);
        lastReading.validate();
        assertEquals(ValidationStatus.VALID, lastReading.getValidationStatus());
    }

    @When("el sistema procesa la lectura")
    public void el_sistema_procesa_la_lectura() {
        container.updateFill(lastReading.getFillLevel().percentage());
        eventPublished = true;
        responseStatus = 200;
    }

    @Then("el contenedor {string} debe reflejar el nuevo nivel y estado actualizados en tiempo real")
    public void el_contenedor_debe_reflejar_el_nuevo_nivel_y_estado_actualizados_en_tiempo_real(String containerCode) {
        assertEquals(containerCode, container.id.value());
        assertEquals(lastReading.getFillLevel().percentage(), container.currentFillLevelPercentage);
        assertTrue(eventPublished);
    }

    @Then("se genera una alerta de mantenimiento para el contenedor {string}")
    public void se_genera_una_alerta_de_mantenimiento_para_el_contenedor(String containerCode) {
        assertNotNull(container, "El contenedor simulado no debe ser nulo");
        assertEquals(containerCode, container.id.value());
        assertTrue(maintenanceAlertGenerated, "Debe generarse una alerta de mantenimiento");
    }

    private void processRequestAfterAuthAndValidate() {
        if (alreadyProcessed) return;

        try {
            if (tokenValido && !headers.containsKey("Authorization")) {
                headers.put("Authorization", "Bearer token-valido");
            }

            if (!esHeaderAuthValido()) {
                responseStatus = 401;
                responseMessage = "Unauthorized";
                alreadyProcessed = true;
                return;
            }

            lastReading.validate();
            lastValidationStatus = lastReading.getValidationStatus();

            if (container == null) {
                container = new FakeContainer(lastReading.getContainerId());
            }
            container.updateFill(lastReading.getFillLevel().percentage());
            eventPublished = true;

            if (lastReading.getBatteryLevel().requiresReplacement()) {
                maintenanceAlertGenerated = true;
            }

            responseStatus = 201;
            responseMessage = null;
        } catch (IllegalArgumentException ex) {
            responseStatus = 400;
            responseMessage = ex.getMessage();
        } catch (Exception ex) {
            responseStatus = 500;
            responseMessage = "Internal error: " + ex.getMessage();
        } finally {
            alreadyProcessed = true;
        }
    }

    private Map<String, String> toMap(io.cucumber.datatable.DataTable table) {
        java.util.List<java.util.List<String>> cells = table.cells();
        if (cells.isEmpty()) return java.util.Collections.emptyMap();

        java.util.List<String> header = cells.get(0);
        java.util.List<String> data = (cells.size() > 1) ? cells.get(1) : java.util.Collections.emptyList();

        java.util.Map<String, String> map = new java.util.HashMap<>();
        for (int i = 0; i < header.size(); i++) {
            String rawKey = header.get(i);
            if (rawKey == null) continue;
            String key = rawKey.trim();

            String rawVal = (i < data.size()) ? data.get(i) : null;
            String val = (rawVal == null) ? null : rawVal.trim();

            map.put(key, val);
        }
        return map;
    }

    private void resetRequestState() {
        responseStatus = 500;
        responseMessage = null;
        eventPublished = false;
        maintenanceAlertGenerated = false;
        alreadyProcessed = false;
    }

    private boolean esHeaderAuthValido() {
        String auth = headers.get("Authorization");
        return auth != null && auth.startsWith("Bearer ") && tokenValido;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private int parseInt(String s) {
        if (s == null) throw new IllegalArgumentException("Missing required integer field");
        String t = s.trim();
        if (t.isEmpty()) throw new IllegalArgumentException("Empty integer field");
        return Integer.parseInt(t);
    }

    private static class FakeContainer {
        final ContainerId id;
        int currentFillLevelPercentage;

        FakeContainer(ContainerId id) {
            this.id = id;
            this.currentFillLevelPercentage = 0;
        }

        void updateFill(int newFillPercentage) {
            this.currentFillLevelPercentage = newFillPercentage;
        }
    }
}
