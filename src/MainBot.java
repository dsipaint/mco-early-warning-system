import java.util.LinkedList;
import java.util.Random;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class MainBot
{
	static JDA jda;
	static final int uppertimer = 50, lowertimer = 30; //no of seconds for bot to wait (has to wait an additional 10s as well)
	static final String prefix = "-";
	static boolean started = true;
	static NameGrabber grabber;
	
	public static void main(String[] args)
	{
		try
		{
			jda = new JDABuilder(AccountType.BOT).setToken("").build();
		}
		catch (LoginException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			jda.awaitReady();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		jda.addEventListener(new CommandListener());
		
		grabber = new NameGrabber(jda);
		Random r = new Random();
		LinkedList<String> names;
		
		//MAIN BOT LOOP
		while(true)
		{
			if(started)
			{
				do
				{
					names = grabber.getNames();
					try
					{
						Thread.sleep(10000);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				while(names == null); //while failed to retrieve names
				
				String warning_msg = grabber.checkNamesForStaff(names);
				if(warning_msg != null)
					jda.getTextChannelById("581576361244753940").sendMessage(warning_msg).queue();
				
				try
				{
					int random = r.nextInt(uppertimer - lowertimer + 1);
					random += lowertimer; //random number between lowertimer and uppertimer
					Thread.sleep(random*1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
