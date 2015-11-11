package rainbowbeard.viaglass;

/**
 Created by Bliss on 11/10/2015.

 Outgoing data: (class)
     Receive image name
     Reverse image search Google API
     Retrieve search results
     Send search results => Search data
 Search data: (class)
     Receive search results
     Filter results for whitelist domains
     If no
     Send no data available
     Send available information to developer
     If yes
     Send filtered addresses => Site parser
 Site parser: (class)
     Receive filtered address
     Extract name
     Send name => Wiki retriever
 */
public class DataExtraction {
}
