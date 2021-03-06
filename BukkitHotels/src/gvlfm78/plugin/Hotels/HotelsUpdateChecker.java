package gvlfm78.plugin.Hotels;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gvlfm78.plugin.Hotels.handlers.HotelsConfigHandler;
import gvlfm78.plugin.Hotels.managers.HotelsMessageManager;

public class HotelsUpdateChecker {
	

	public HotelsUpdateChecker(){}
	HotelsMain plugin = new HotelsMain();
	HotelsMessageManager HMM = new HotelsMessageManager();
	HotelsConfigHandler HConH = new HotelsConfigHandler();
	
	private URL filesFeed;

	private String version;
	private String link;

	public HotelsUpdateChecker(String url){

		try{
			this.filesFeed = new URL(url);
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
	}

	public boolean updateNeeded(){
		try {
			InputStream input = this.filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

			Node latestFile = document.getElementsByTagName("item").item(0);
			NodeList children = latestFile.getChildNodes();

			this.version = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
			this.link = children.item(3).getTextContent();

			if(versionCompare(plugin.getDescription().getVersion(),this.version)<0){
				return true;
			}
		} catch (UnknownHostException uhe){
			plugin.getServer().getLogger().severe(HMM.mes("main.noConnection"));
		} catch (Exception e) {
			e.printStackTrace();
		}


		return false;
	}

	public String getVersion(){
		return this.version;
	}

	public String getLink(){
		return this.link;
	}

	public Integer versionCompare(String oldVer, String newVer)
	{
		String[] vals1 = oldVer.split("\\.");
		String[] vals2 = newVer.split("\\.");
		int i = 0;
		// set index to first non-equal ordinal or length of shortest version string
		while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) 
		{
			i++;
		}
		// compare first non-equal ordinal number
		if (i < vals1.length && i < vals2.length) 
		{
			int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}
		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		else
		{
			return Integer.signum(vals1.length - vals2.length);
		}
	}
}

