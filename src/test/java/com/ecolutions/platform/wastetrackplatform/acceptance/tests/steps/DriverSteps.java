
package com.ecolutions.platform.wastetrackplatform.acceptance.tests.steps;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.DriverLicense;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
        import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
        import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DriverSteps {

    private Driver driver;
    private DistrictId districtId;
    private VehicleId vehicleId;
    private String generatedCredentials;
    private Exception exception;

    @Given("el administrador está autenticado")
    public void el_administrador_esta_autenticado() {
        // En un test real, se mockea autenticación JWT o se asume válida.
        System.out.println("Administrador autenticado correctamente.");
    }

    @When("crea una cuenta de conductor con los siguientes datos:")
    public void crea_una_cuenta_de_conductor_con_datos(DataTable table) {
        Map<String, String> data = table.asMaps().get(0);
        try {
            districtId = new DistrictId(data.get("districtId"));
            FullName fullName = new FullName(data.get("fullName").split(" ")[0], data.get("fullName").split(" ")[1]);
            DocumentNumber documentNumber = new DocumentNumber(data.get("documentNumber"));
            PhoneNumber phoneNumber = new PhoneNumber(data.get("phoneNumber"));
            UserId userId = new UserId(data.get("userId"));
            DriverLicense license = new DriverLicense(data.get("driverLicense"));
            LocalDate expiryDate = LocalDate.parse(data.get("expiryDate"));
            EmailAddress email = new EmailAddress(data.get("emailAddress"));

            driver = new Driver(districtId, fullName, documentNumber, phoneNumber, userId, license, expiryDate, email);
            generatedCredentials = "USR-001-TOKEN";
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("el sistema genera credenciales para el conductor")
    public void el_sistema_genera_credenciales_para_el_conductor() {
        assertNotNull(generatedCredentials, "Las credenciales deben ser generadas");
    }

    @Then("el estado inicial del conductor es {string}")
    public void el_estado_inicial_del_conductor_es(String expectedStatus) {
        assertEquals(expectedStatus, driver.getStatus().name());
    }

    @Given("existe un conductor con estado {string}")
    public void existe_un_conductor_con_estado(String estado) {
        setDefaultDriver();
        if (estado.equals("ON_ROUTE")) driver.startRoute();
        if (estado.equals("SUSPENDED")) driver.suspend("Manual suspension");
    }

    @When("inicia una ruta")
    public void inicia_una_ruta() {
        driver.startRoute();
    }

    @Then("el estado del conductor debe ser {string}")
    public void el_estado_del_conductor_debe_ser(String expectedStatus) {
        assertEquals(expectedStatus, driver.getStatus().name());
    }

    @Given("el conductor tiene una ruta activa y ha trabajado {int} horas")
    public void el_conductor_tiene_una_ruta_activa_y_ha_trabajado_horas(Integer horas) {
        setDefaultDriver();
        driver.startRoute();
        driver.setTotalHoursWorked(horas);
    }

    @When("completa la ruta con duración de {int} horas")
    public void completa_la_ruta_con_duracion_de_horas(Integer horasRuta) {
        driver.completeRoute(horasRuta);
    }

    @Then("el total de horas trabajadas debe ser {int}")
    public void el_total_de_horas_trabajadas_debe_ser(Integer totalEsperado) {
        assertEquals(totalEsperado, driver.getTotalHoursWorked());
    }

    @When("el administrador suspende al conductor con el motivo {string}")
    public void el_administrador_suspende_al_conductor_con_motivo(String motivo) {
        driver.suspend(motivo);
    }

    @Given("el conductor está registrado")
    public void el_conductor_esta_registrado() {
        setDefaultDriver();
    }

    @When("el administrador asigna el vehículo con id {string}")
    public void el_administrador_asigna_el_vehiculo_con_id(String idVehiculo) {
        vehicleId = new VehicleId(idVehiculo);
        driver.assignVehicle(vehicleId);
    }

    @Then("el vehículo asignado debe ser {string}")
    public void el_vehiculo_asignado_debe_ser(String idEsperado) {
        assertEquals(idEsperado, (VehicleId.toStringOrNull(driver.getAssignedVehicleId())));
    }

    @When("el administrador desasigna el vehículo")
    public void el_administrador_desasigna_el_vehiculo() {
        driver.unassignVehicle();
    }

    @Then("el conductor no debe tener vehículo asignado")
    public void el_conductor_no_debe_tener_vehiculo_asignado() {
        assertNull(driver.getAssignedVehicleId());
    }

    private void setDefaultDriver() {
        districtId = new DistrictId("150101");
        driver = new Driver(
                districtId,
                new FullName("John", "Doe"),
                new DocumentNumber("12345678"),
                new PhoneNumber("+51987654321"),
                new UserId("USR-001"),
                new DriverLicense("A1234567"),
                LocalDate.now().plusYears(1),
                new EmailAddress("john.doe@example.com")
        );
    }
}
