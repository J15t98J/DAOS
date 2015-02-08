package uk.co.appsbystudio.damealiceowens.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RSSFeedParser {

	public ArrayList<RSSItem> get() {
		try {
			URL rssLoc = new URL("http", "appsbystudio.co.uk", 80, "test.rss");
			String data = rssLoc.getFile();
			// TODO: Parse XML file into RSSItem objects
		} catch(MalformedURLException e) {
			// TODO: Show a "News cannot be loaded." page
		}

		ArrayList<RSSItem> temp = new ArrayList<RSSItem>();
		// TODO: Remove these, they're just examples for testing
		temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
		temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
		temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));
        temp.add(new RSSItem("DT SysCon deadline", "08/02/15 by DT Dept.", false, false));
        temp.add(new RSSItem("Finish early next Friday", "07/02/15 by Office", false, false));
        temp.add(new RSSItem("Something else", "06/02/15 by PE Dept.", true, false));

		return temp;
	}
}