# CheckMate
Intellij Plugin to discover unchecked exceptions that might be thrown in the methods you selected.  
Option to create/change a try catch statement for the selcted exceptions.  
Option to include @throws clauses from JavaDocs in search results.  
Option to include Errors in search results.  
Option to search for exceptions through all overrides of an encountered method.  
Option to whitelist java*, org.xml*, org.omg* as sources to search through (they are ignored by default).  
Option to blacklist certain exceptions from search results (i.e. NullPointerException).  
