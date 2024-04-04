package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.it.Ma;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.CommonMethods;
import utils.ConfigReader;
import utils.Constans;
import utils.ExcelReader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddEmployeeSteps extends CommonMethods {


    @When("user clicks on PIM option")
    public void user_clicks_on_pim_option() {
        //WebElement pimOption = driver.findElement(By.id("menu_pim_viewPimModule"));
       // pimOption.click();
        click(dashboard.pimOption);
    }
    @When("user clicks on Add Employee button")
    public void user_clicks_on_add_employee_button() {
       // WebElement addEmployeeButton = driver.findElement(By.id("menu_pim_addEmployee"));
        //addEmployeeButton.click();
        click(dashboard.addEmployeeOption);
    }
    @When("user enter firstname and lastname")
    public void user_enter_firstname_and_lastname() {
       //WebElement firstName = driver.findElement(By.id("firstName"));
       //firstName.sendKeys("Tomas");

        sendText(addEmployee.firstNameField, "joshpan");

        //WebElement lastName = driver.findElement(By.id("lastName"));
        //lastName.sendKeys("Shelby");
        sendText(addEmployee.lastNameField, "vernulla");
    }
    @When("user clicks on save button")
    public void user_clicks_on_save_button() {
       //WebElement saveButton = driver.findElement(By.id("btnSave"));
       // saveButton.click();

        click(addEmployee.saveButton);
    }
    @Then("employee added successfully")
    public void employee_added_successfully() {
        System.out.println("Employee Added");
    }

    @When("user enter {string} and {string}")
    public void user_enter_and(String firstName, String lastName) {
        sendText(addEmployee.firstNameField, firstName);
        sendText(addEmployee.lastNameField, lastName);
    }

    @When("user enter {string} and {string} for adding multiple employees")
    public void user_enter_and_for_adding_multiple_employees(String firstNameValue, String lastNameValue) {
        sendText(addEmployee.firstNameField, firstNameValue);
        sendText(addEmployee.lastNameField, lastNameValue);
    }

    @When("user adds multiple employee and verify they are added successfully")
    public void user_adds_multiple_employee_and_verify_they_are_added_successfully(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> employeeNames = dataTable.asMaps();

        for (Map<String, String> employee: employeeNames
        ) {
            String firstNameValue = employee.get("firstName");
            String middleNameValue = employee.get("middleName");
            String lastNameValue = employee.get("lastName");

            click(addEmployee.saveButton);
            Thread.sleep(2000);

            click(dashboard.addEmployeeOption);
            Thread.sleep(2000);

        }
    }

    @When("user adds multiple employee from excel using {string} and verify it")
    public void user_adds_multiple_employee_from_excel_using_and_verify_it(String sheetName) throws InterruptedException {


        List<Map<String, String>> empFromExcel =
                ExcelReader.excelListIntoMap(Constans.TESTDATA_FILEPATH, sheetName);

        Iterator<Map<String, String>> itr = empFromExcel.iterator();
        while(itr.hasNext()){

            Map<String, String> mapNewEmp = itr.next();

            sendText(addEmployee.firstNameField, mapNewEmp.get("firstName"));
            sendText(addEmployee.middleNameField, mapNewEmp.get("middleName"));
            sendText(addEmployee.lastNameField, mapNewEmp.get("lastName"));
            String empIdValue = addEmployee.empIdLocator.getAttribute("value");
            sendText(addEmployee.photograph, mapNewEmp.get("photograph"));

            if(!addEmployee.checkBox.isSelected()){
                click(addEmployee.checkBox);
            }

            sendText(addEmployee.createusernameField, mapNewEmp.get("username"));
            sendText(addEmployee.createpasswordField, mapNewEmp.get("password"));
            sendText(addEmployee.confirmpasswordField, mapNewEmp.get("confirmPassword"));

            //verify

            click(addEmployee.saveButton);
            Thread.sleep(2000);
            click(dashboard.empListOption);
            Thread.sleep(2000);

            sendText(employeeList.empSearchIdField, empIdValue);
            click(employeeList.searchButton);

            List<WebElement> rowData =
                    driver.findElements(By.xpath("//*[@id='resultTable']/tbody/tr"));

            for (int i =0; i<rowData.size(); i++){
                System.out.println("I am inside the loop and worried about josh");
                //getting the text of every element from here and storing it into string
                String rowText = rowData.get(i).getText();
                System.out.println(rowText);

                String expectedData = empIdValue + " " + mapNewEmp.get("firstName")
                        + " " + mapNewEmp.get("middleName") + " " + mapNewEmp.get("lastName");

                //verifying the exact details  of the employee
                Assert.assertEquals(expectedData, rowText);

            }
            Thread.sleep(2000);
            click(dashboard.addEmployeeOption);
            Thread.sleep(2000);
        }
    }
    }