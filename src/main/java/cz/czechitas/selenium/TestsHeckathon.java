package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestsHeckathon {

    public static final String URL = "http://czechitas-datestovani-hackathon.cz/en/";
    public static final String EMAIL = "testHackathon@seznam.cz";
    public static final String WRONGEMAIL = "test@seznam.cz";
    public static final String PASSWORD = "Th1234!";
    public static final String WRONGPASSWORD = "Th1234";
    public static final String NAME = "Test";
    public static final String LAST_NAME = "Hackathon";
    //Před každým spuštěním testu vložit unikátní NEW_EMAIL
    public static final String NEW_EMAIL = "k@k.cz";
    public static final String FIRST_NAME_REG = "Adam";
    public static final String LAST_NAME_REG = "Adams";
    public static final String PASSWORD_REG = "12345";
    public static final String HOTEL_NAME = "Three Foxes Lounge";
    public static final int CHECK_IN_DATE = 21;
    public static final int CHECK_OUT_DATE = 23;
    public static final String ROOM_NAME = "Deluxe apartments";
    public static final String ADDRESS = "Diagon Alley";
    public static final String CITY = "London";
    public static final int ZIP_CODE = 111111;
    public static final String COUNTRY = "United Kingdom";
    public static final long HOME_PHONE = 123456789;
    public static final long MOBILE_PHONE = 123456789;
    public static final String ADDRESS_TITLE = "My address";


    WebDriver browser;
    WebDriverWait wait;

    @BeforeEach
    public void setUp() {
//      System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/Java-Training/Selenium/geckodriver");
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        browser = new FirefoxDriver();
//        browser.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait = new WebDriverWait(browser, 10);
    }

    @Test
    public void registeredUserLogInSuccessfully() {
        browser.navigate().to("http://czechitas-datestovani-hackathon.cz/en/");
        clickSignInButtonNavigation();
        insertLoginData(EMAIL, PASSWORD);
        waitForMyAccountPage();
        WebElement navigationUserLogIn = browser.findElement(By.xpath("//span[contains(@class, 'account_user_name')]"));
        Assertions.assertEquals(NAME, navigationUserLogIn.getText());
        logOut();
    }

    @Test
    public void registeredUserUnsuccessfullyLogInDueWrongPassword() {
        browser.navigate().to("http://czechitas-datestovani-hackathon.cz/en/");
        clickSignInButtonNavigation();
        insertLoginData(EMAIL, WRONGPASSWORD);
        waitForAlertText();
        WebElement alertMessage = browser.findElement(By.xpath("//li[contains(text(), 'Authentication failed.')]"));
        Assertions.assertEquals("Authentication failed.", alertMessage.getText());
    }

    @Test
    public void registeredUserUnsuccessfullyLogInNoPasswordFilledIn() {
        browser.navigate().to("http://czechitas-datestovani-hackathon.cz/en/");
        clickSignInButtonNavigation();
        insertLoginData(EMAIL, "");
        waitForAlertText();
        WebElement alertMessage = browser.findElement(By.xpath("//li[contains(text(), 'Password is required.')]"));
        Assertions.assertEquals("Password is required.", alertMessage.getText());
    }

    @Test
    public void registeredUserUnsuccessfullyLogInDueToWrongEmail() {
        browser.navigate().to("http://czechitas-datestovani-hackathon.cz/en/");
        clickSignInButtonNavigation();
        insertLoginData(WRONGEMAIL, PASSWORD);
        waitForAlertText();
        WebElement alertMessage = browser.findElement(By.xpath("//li[contains(text(), 'Authentication failed.')]"));
        Assertions.assertEquals("Authentication failed.", alertMessage.getText());
    }

    @Test
    public void registeredUserLogInSuccessfullyAndLogOutSuccessfully() {
        browser.navigate().to("http://czechitas-datestovani-hackathon.cz/en/");
        clickSignInButtonNavigation();
        insertLoginData(EMAIL, PASSWORD);
        waitForMyAccountPage();
        logOut();
        WebElement navigationSignIn = browser.findElement(By.xpath("//span[@class='hide_xs']"));
        Assertions.assertEquals("Sign in", navigationSignIn.getText());
    }

    @Test
    public void newUserIsSuccessfullyRegistrated() {
        browser.navigate().to("http://czechitas-datestovani-hackathon.cz/en/");
        clickSignInButtonNavigation();
        WebElement inputEmailRegistration = browser.findElement(By.id("email_create"));
        inputEmailRegistration.sendKeys(NEW_EMAIL);
        WebElement buttonCreateAccount = browser.findElement(By.id("SubmitCreate"));
        buttonCreateAccount.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Create an account')]")));
        fillInRegistrationForm(FIRST_NAME_REG, LAST_NAME_REG, PASSWORD_REG);
        waitForMyAccountPage();
        WebElement alertAccountCreated = browser.findElement(By.xpath("//p[contains(@class,'alert-success')]"));

        Assertions.assertEquals("Your account has been created.", alertAccountCreated.getText());
        logOut();

    }

    @Test
    public void userOrderRoomSuccessfully(){
        browser.navigate().to("http://czechitas-datestovani-hackathon.cz/en/");
        enterDatesForSearchingRoom(HOTEL_NAME, CHECK_IN_DATE, CHECK_OUT_DATE);
        chooseRoom(ROOM_NAME);
        proceedToCheckout();
        loginAfterCheckout(NEW_EMAIL,PASSWORD);
        fillInAddress(NAME,LAST_NAME,ADDRESS, CITY, ZIP_CODE, HOME_PHONE, MOBILE_PHONE, ADDRESS_TITLE);
        payment();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(@class,'alert')]")));
        WebElement orderConfirmation = browser.findElement(By.xpath("//p[contains(@class,'alert')]"));

        Assertions.assertEquals("Your order on Czechitas DA Hackathon is complete.", orderConfirmation.getText());
        logOut();

    }


    @AfterEach
    public void tearDown() {
        browser.quit();
    }

    public void clickSignInButtonNavigation() {

        WebElement buttonSignInNavigation = browser.findElement(By.xpath("//span[@class='hide_xs']"));
        buttonSignInNavigation.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Authentication')]")));
    }

    public void insertLoginData(String email, String password) {
        WebElement inputEmail = browser.findElement(By.id("email"));
        inputEmail.sendKeys(email);
        WebElement inputPassword = browser.findElement(By.id("passwd"));
        inputPassword.sendKeys(password);
        WebElement buttonSignIn = browser.findElement(By.xpath("//i[contains(@class,'icon-lock')]"));
        buttonSignIn.click();

    }

    public void waitForMyAccountPage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'My account')]")));
    }

    public void waitForAlertText() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'alert')]")));
    }

    public void logOut() {
        WebElement buttonUserMenu = browser.findElement(By.id("user_info_acc"));
        buttonUserMenu.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='Log me out']")));
        JavascriptExecutor js = (JavascriptExecutor) browser;

        WebElement buttonLogMeOut = browser.findElement(By.xpath("//a[@title='Log me out']"));
        js.executeScript("arguments[0].click();", buttonLogMeOut);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='hide_xs']")));
        WebElement buttonSignInNavigation = browser.findElement(By.xpath("//span[@class='hide_xs']"));
    }

    public void fillInRegistrationForm(String firstName, String lastName, String password) {
        WebElement inputFirstName = browser.findElement(By.id("customer_firstname"));
        inputFirstName.sendKeys(firstName);
        WebElement inputLastName = browser.findElement(By.id("customer_lastname"));
        inputLastName.sendKeys(lastName);
        WebElement inputPassword = browser.findElement(By.id("passwd"));
        inputPassword.sendKeys(password);
        WebElement inputRegister = browser.findElement(By.xpath("//button//*[contains(text(),'Register')]"));
        inputRegister.click();
    }

    public void enterDatesForSearchingRoom(String hotelName, int checkInDate, int checkOutDate){
        WebElement buttonHotel = browser.findElement(By.id("id_hotel_button"));
        buttonHotel.click();
        WebElement buttonChosenHotel = browser.findElement(By.xpath("//li[contains(text(), '" + hotelName + "')]")) ;
        buttonChosenHotel.click();
        WebElement buttonCheckIn = browser.findElement(By.id("check_in_time"));
        buttonCheckIn.click();
        WebElement buttonChosenCheckIn = browser.findElement(By.xpath("//tr//a[contains(text(),'" + String.valueOf(checkInDate) + "')]"));
        buttonChosenCheckIn.click();
        WebElement buttonCheckOut = browser.findElement(By.id("check_out_time"));
        buttonCheckOut.click();
        WebElement buttonChosenCheckOut = browser.findElement(By.xpath("//tr//a[contains(text(),'" + String.valueOf(checkOutDate) + "')]"));
        buttonChosenCheckOut.click();
        WebElement buttonSearchRoom = browser.findElement(By.id("search_room_submit")) ;
        buttonSearchRoom.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'Sort By:')]")));
    }

        public void chooseRoom(String roomName) {
            WebElement buttonChoosenRoomBook = browser.findElement(By.xpath("//div[p[contains(text(), '" + roomName + "')]]//span[contains(text(), 'Book Now')]"));
            buttonChoosenRoomBook.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(., 'Room successfully added')]")));
        }

        public void proceedToCheckout(){
        WebElement buttonProceedCheckout = browser.findElement(By.xpath("//span[contains(., 'Proceed to checkout')]")) ;
        buttonProceedCheckout.click();

        WebElement buttonProceed = browser.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/section/div/section/div/div[1]/div/div[1]/div[2]/div/div[2]/div[2]/div/a/span"));
        buttonProceed.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Price Summary')]")));
        WebElement buttonLogIn = browser.findElement(By.id("openLoginFormBlock"));
        buttonLogIn.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login_email"))) ;
    }
        public void loginAfterCheckout(String email, String password){
            WebElement inputEmail = browser.findElement(By.id("login_email"));
            inputEmail.sendKeys(email);
            WebElement inputPassword = browser.findElement(By.id("login_passwd"));
            inputPassword.sendKeys(password);
            WebElement buttonSignIn = browser.findElement(By.xpath("//i[contains(@class,'icon-lock')]"));
            buttonSignIn.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Your addresses')]")));
    }
    public void fillInAddress (String firstName, String lastName, String address, String city, int zipCode, long homePhone, long mobilePhone, String addressTitle) {
        WebElement inputFirstName = browser.findElement(By.id("firstname"));
        inputFirstName.sendKeys(firstName);
        WebElement inputLastName = browser.findElement(By.id("lastname"));
        inputLastName.sendKeys(lastName);
        WebElement inputAddress = browser.findElement(By.id("address1"));
        inputAddress.sendKeys(address);
        WebElement inputCity = browser.findElement(By.id("city"));
        inputCity.sendKeys(city);
        WebElement inputZipCode = browser.findElement(By.id("postcode"));
        inputZipCode.sendKeys(String.valueOf(zipCode));


        WebElement inputHomePhone = browser.findElement(By.id("phone"));
        inputHomePhone.sendKeys(String.valueOf(homePhone));
        WebElement inputMobilePhone = browser.findElement(By.id("phone_mobile"));
        inputMobilePhone.sendKeys(String.valueOf(mobilePhone));
        WebElement inputAddressTitle = browser.findElement(By.id("alias"));
        inputAddressTitle.sendKeys(addressTitle);
        WebElement buttonSave = browser.findElement(By.xpath("//span[contains(text(),'Save')]"));
        buttonSave.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Guest Information')]")));
    }

        public void payment(){
        WebElement checkBoxAgreement = browser.findElement(By.id("cgv"));
        checkBoxAgreement.click();
        WebElement payment = browser.findElement(By.xpath("//a[@class='cheque']"));
        payment.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'I confirm my order')]")));
        WebElement confirm = browser.findElement(By.xpath("//span[contains(text(),'I confirm my order')]"));
        confirm.click();



    }



}
