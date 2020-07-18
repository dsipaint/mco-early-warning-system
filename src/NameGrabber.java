
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import net.dv8tion.jda.core.JDA;

public class NameGrabber
{
	
	private WebDriver driver;
	private boolean staffAlreadyOnline;
	private LinkedList<String> commies;
	private int commieCount;
	private JDA jda;
	
	public NameGrabber(JDA jda)
	{
		//SETUP
		System.setProperty("webdriver.chrome.driver","C:\\javaclasses\\libs\\Selenium\\chromedriver.exe");
		driver = new ChromeDriver();
		this.jda = jda;
		
		staffAlreadyOnline = false;
		commieCount = 0;
		
		BufferedReader br = null;
		commies = new LinkedList<String>();
		try
		{
			br = new BufferedReader(new FileReader("./commies.txt")); //set up a reader to read staff names
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("file ./commies.txt not found");
		}
		
			//EXTRACT COMMIE NAMES
		while(true)
		{
			String str = null;
			
			try
			{
				str = br.readLine(); //read next commie name
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			if(str == null)
				break; //end loop if the whole file is read
			else
				commies.add(str); //add commie name to linkedlist
		}
		
		try
		{
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		jda.getTextChannelById("581576361244753940").sendMessage("***Early Warning System initialized***").queue();
	}
	
	public LinkedList<String> getNames() //retrieve player names from website
	{
		LinkedList<String> names = new LinkedList<String>();
		List<WebElement> nameElements = null;
		
		/*//MINECRAFTSTATISTICS TECHNIQUE
		driver.get("https://minecraft-statistic.net/en/server/MinecraftOnline.html");
		nameElements = driver.findElements(By.className("c-black"));
		if(nameElements == null || nameElements.size() == 0) //failed to retrieve names
			return null;
		
		for(int i = 0; i < nameElements.size(); i++)
		{
			String nametxt = nameElements.get(i).getText();
			names.add(nametxt);
		}
		*/
		
		//MINECRAFTONLINE TECHNIQUE
		driver.get("http://minecraftonline.com");
		List <WebElement> leftSideElements = driver.findElements(By.className("leftsidebox"));
		for(int i = 0; i < leftSideElements.size(); i++) //there are multiple boxes with the class "leftsidebox"
		{
			WebElement tempElement = leftSideElements.get(i);
			if(tempElement.findElement(By.tagName("h3")).getText().equals("Players online")) //finding the leftsidebox we actually care about
			{
				nameElements = tempElement.findElements(By.tagName("a"));
				break;
			}
		}
		
		if(nameElements == null || nameElements.size() == 0) //failed to retrieve names
			return null;
		
		for(int i = 0; i < nameElements.size(); i++)
		{
			String nametxt = nameElements.get(i).getText();
			names.add(nametxt);
		}
		
		return names;
	}
	
	public String checkNamesForStaff(LinkedList<String> names) //check current players for commies
	{
		String output = null;
		boolean commiesOnline = false;
		String commiesOnlineNames = "";
		int currentCommieCount = 0;
		
		for(int i = 0; i < names.size(); i++) //scan through and check for online commies
		{
			for(int j = 0; j < commies.size(); j++)
			{
				if(names.get(i).equals(commies.get(j)))
				{
					commiesOnline = true;
					commiesOnlineNames += names.get(i) + ", ";
					currentCommieCount++; //count number of commies online
				}
			}
		}
		
		if(currentCommieCount > 0)  //string formatting- dw about it
			commiesOnlineNames = commiesOnlineNames.substring(0, commiesOnlineNames.length() - 2);
		
		if(commiesOnline && !staffAlreadyOnline)
		{
			output = "Commies have joined!";
			staffAlreadyOnline = true;
		}
		if(currentCommieCount != commieCount)
		{
			if(currentCommieCount > commieCount)
				output = "Commie(s) joined. New commie list:\n" + commiesOnlineNames;
			if(currentCommieCount < commieCount && currentCommieCount != 0)
				output = "Commie(s) left. New commie list:\n" + commiesOnlineNames;
			
			commieCount = currentCommieCount;
		}
		else if(!commiesOnline && staffAlreadyOnline)
		{
			output = "Glorious FDE rise up! No commies are online " + jda.getGuildById("570078313402335253").getMemberById("456636000073023521").getAsMention();
			staffAlreadyOnline = false;
		}
		
		return output;
	}
	
	public void stopGrabber()
	{
		driver.close();
	}
	
}
