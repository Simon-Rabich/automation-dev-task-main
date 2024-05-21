package com.antelope.af.utilities;


public class HTMLParser {
	
	/**
	 * Convert Plain Text to its Html Form
	 * @param s - string to be converted
	 * 
	 */
	public static String convertPlainTextToHTML(String s){
		StringBuilder builder = new StringBuilder();
	    boolean previousWasASpace = false;
	    for( char c : s.toCharArray() ) {
	        if( c == ' ' ) {
	            if( previousWasASpace ) {
	                builder.append("&nbsp;");
	                previousWasASpace = false;
	                continue;
	            }
	            previousWasASpace = true;
	        } else {
	            previousWasASpace = false;
	        }
	        switch(c) {
	            case '<': builder.append("&lt;"); break;
	            case '>': builder.append("&gt;"); break;
	            case '&': builder.append("&amp;"); break;
	            case '"': builder.append("&quot;"); break;
	            case '\n': builder.append("<br>"); break;
	            // We need Tab support here, because we print StackTraces as HTML
	            case '\t': builder.append("&nbsp; &nbsp; &nbsp;"); break;  
	            default:
	                    builder.append(c);
	        }
	    }
	    return builder.toString();
	}
	
	public static String asciiConvertor(String str){
		StringBuilder builder = new StringBuilder();
	    for( char c : str.toCharArray() ) {
	    	switch(c) {
	    		case ' ': builder.append("%20"); break;
	    		case '"': builder.append("%22"); break;
	    		case '#': builder.append("%23"); break;
	    		case '$': builder.append("%24"); break;
	    		case '%': builder.append("%25"); break;
	    		case '&': builder.append("%26"); break;
	    		case '\'': builder.append("%27"); break;
	    		case '/': builder.append("%2F"); break;
	    		case '(': builder.append("%28"); break;
	    		case ')': builder.append("%29"); break;
	    		case '*': builder.append("%2A"); break;
	    		case '-': builder.append("%2D"); break;
	    		case '+': builder.append("%2B"); break;
	    		default:
	    			builder.append(c);
	    	}
	    }
		return builder.toString();
	}

}
