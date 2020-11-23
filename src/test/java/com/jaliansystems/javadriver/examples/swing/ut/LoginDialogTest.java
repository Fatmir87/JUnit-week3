package com.jaliansystems.javadriver.examples.swing.ut;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.sourceforge.marathon.javadriver.JavaDriver;
import net.sourceforge.marathon.javadriver.JavaProfile;
import net.sourceforge.marathon.javadriver.JavaProfile.LaunchMode;
import net.sourceforge.marathon.javadriver.JavaProfile.LaunchType;

public class LoginDialogTest {

	private LoginDialog login;
	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		login = new LoginDialog() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSuccess() {
			}

			@Override
			protected void onCancel() {
			}
		};
		SwingUtilities.invokeLater(() -> login.setVisible(true));
		JavaProfile profile = new JavaProfile(LaunchMode.EMBEDDED);
		profile.setLaunchType(LaunchType.SWING_APPLICATION);
		driver = new JavaDriver(profile);
	}

	@After
	public void tearDown() throws Exception {
		if (login != null)
			SwingUtilities.invokeAndWait(() -> login.dispose());
		if (driver != null)
			driver.quit();
	}

	@Test
	public void loginSuccess() {
		WebElement user = driver.findElement(By.cssSelector("text-field"));
		user.sendKeys("bob");
		WebElement pass = driver.findElement(By.cssSelector("password-field"));
		pass.sendKeys("secret");
		WebElement loginBtn = driver.findElement(By.cssSelector("button[text='Login']"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
		loginBtn.click();
		assertTrue(login.isSucceeded());
		assertNotNull(login.getSize());
	}

	@Test
	public void loginCancel() {
		WebElement user = driver.findElement(By.cssSelector("text-field"));
		user.sendKeys("bob");
		WebElement pass = driver.findElement(By.cssSelector("password-field"));
		pass.sendKeys("secret");
		WebElement cancelBtn = driver.findElement(By.cssSelector("button[text='Cancel']"));
		cancelBtn.click();
		assertFalse(login.isSucceeded());
	}

	@Test
	public void loginInvalid() throws InterruptedException {
		WebElement user = driver.findElement(By.cssSelector("text-field"));
		user.sendKeys("bob");
		WebElement pass = driver.findElement(By.cssSelector("password-field"));
		pass.sendKeys("wrong");
		WebElement loginBtn = driver.findElement(By.cssSelector("button[text='Login']"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
		loginBtn.click();
		driver.switchTo().window("Invalid Login");
		driver.findElement(By.cssSelector("button[text='OK']")).click();
		driver.switchTo().window("Login");
		user = driver.findElement(By.cssSelector("text-field"));
		pass = driver.findElement(By.cssSelector("password-field"));
		assertTrue(user.getText().isEmpty());
		assertTrue(pass.getText().isEmpty());
	}

	// uppgift 2
	@Test
	public void getPass() throws InterruptedException {

		WebElement pass = driver.findElement(By.cssSelector("password-field"));

		WebElement getPassBtn = driver.findElement(By.cssSelector("button[text='getPass']"));
		getPassBtn.click();
		assertEquals("secret", pass.getText());

	}

	@Test
	public void checkTooltipText() {

		List<WebElement> textComponents = driver.findElements(By.className(JTextComponent.class.getName()));
		for (WebElement tc : textComponents) {
			assertNotEquals(null, tc.getAttribute("toolTipText"));
		}
	}
	
	// BONUS UPPGIFT
	
	// I build.gradle under dependencies på testImplementation så kan vi se att versionen för Junit är 4.12
	

}