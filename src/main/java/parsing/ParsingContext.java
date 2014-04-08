package parsing;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This object represents a part of a file with the content and defines which 
 * Text context it is (Text, Php, Comment, etc.) 
 * 
 */
public class ParsingContext {
	private ParsingContextType context;
	private String content;
	private String filePath;
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ParsingContext(ParsingContextType context) {
		this.context = context;
	}
	
	public ParsingContext(ParsingContextType context, int beginIndex, int endIndex, String content) {
		this.context = context;
		this.setContent(content.substring(beginIndex, endIndex));
	}
	public ParsingContextType getContext() {
		return context;
	}
	public void setContext(ParsingContextType context) {
		this.context = context;
	}
	public String getFilePath() {
		return filePath;
	}	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
