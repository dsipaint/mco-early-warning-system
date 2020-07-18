import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		if(e.getChannel().getId().equals("581576361244753940") && !e.getAuthor().isBot())
		{
			if(e.getMessage().getContentRaw().equals(MainBot.prefix + "start"))
			{
				MainBot.started = true;
				e.getChannel().sendMessage("***Bot activities resumed***").queue();
			}
			if(e.getMessage().getContentRaw().equals(MainBot.prefix + "pause"))
			{
				MainBot.started = false;
				e.getChannel().sendMessage("***Bot paused***").queue();
			}
			
			if(e.getMessage().getContentRaw().equals(MainBot.prefix + "stop") && e.getAuthor().getId().equals("161100213052768257"))
			{
				e.getChannel().sendMessage("***Shutting down***").queue();
				MainBot.grabber.stopGrabber();
				System.exit(0); //only I can stop the bot completely
			}
			if(e.getMessage().getContentRaw().equals(MainBot.prefix + "stop") && !e.getAuthor().getId().equals("161100213052768257"))
				e.getChannel().sendMessage("You are not authorised to do that " + e.getAuthor().getAsMention() + "!").queue();
		}
	}
}
