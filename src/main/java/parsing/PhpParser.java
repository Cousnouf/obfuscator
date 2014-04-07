package parsing;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Self made Php Parser which split the content of files into one or several 
 * contexts. 
 * 
 */

import java.util.ArrayList;
import java.util.List;

public class PhpParser {
	public static final String START_OF_PHP_CONTEXT = "<?"; 
	public static final String END_OF_PHP_CONTEXT = "?>"; 
	public static final String START_OF_LINE_COMMENT_CONTEXT = "//"; 
	public static final String END_OF_LINE_COMMENT_CONTEXT = "\n"; 
	public static final String START_OF_COMMENT_CONTEXT = "/*"; 
	public static final String END_OF_COMMENT_CONTEXT = "*/"; 
	public static final char ARRAY_VARIABLE_START_CHARACTER = '[';
	public static final char ARRAY_VARIABLE_END_CHARACTER = ']';
	public static final String START_OF_DYNAMIC_TEXT_CONTEXT = "\""; 
	public static final String END_OF_DYNAMIC_TEXT_CONTEXT = "\""; 
	public static final String START_OF_TEXT_CONTEXT = "\'"; 
	public static final String END_OF_TEXT_CONTEXT = "\'"; 
	public static final char ESCAPE_CHARACTER = '\\';
	
	private List<ParsingContext> fileContextContentList;
	
	public List<ParsingContext> parsePhpContent (String content) {
		fileContextContentList = new ArrayList<ParsingContext>();
		ParsingContextType currentEnumeratedContext;
		if (content.indexOf(START_OF_PHP_CONTEXT) > 0) {
			currentEnumeratedContext = ParsingContextType.Text;
		} else {
			currentEnumeratedContext = ParsingContextType.Php;			
		}
		boolean parseFile = true;
		int currentCharIndex = 0;
		int previousBeginIndex = 0;
		while (parseFile) {
			currentCharIndex++;	
			if (currentCharIndex >= content.length()) {
				previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, content.length(), content);
				parseFile = false;
			} else if (currentEnumeratedContext == ParsingContextType.Php) {
				if (content.charAt(currentCharIndex) == END_OF_PHP_CONTEXT.charAt(0)) {
					currentCharIndex++;		
					if (content.charAt(currentCharIndex) == END_OF_PHP_CONTEXT.charAt(1)) {
						previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, ++currentCharIndex, content);
						currentEnumeratedContext = ParsingContextType.Text;
					}
				} else if (content.charAt(currentCharIndex) == START_OF_TEXT_CONTEXT.charAt(0) &&
						content.charAt(currentCharIndex - 1) != ARRAY_VARIABLE_START_CHARACTER &&
						(content.charAt(currentCharIndex + 1) != ARRAY_VARIABLE_END_CHARACTER ||
						!Character.isJavaIdentifierPart(content.charAt(currentCharIndex - 1)))) { 
					previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, currentCharIndex, content);
					currentEnumeratedContext = ParsingContextType.PhpText;
				} else if (content.charAt(currentCharIndex) == START_OF_DYNAMIC_TEXT_CONTEXT.charAt(0) &&
						content.charAt(currentCharIndex - 1) != ARRAY_VARIABLE_START_CHARACTER &&
						(content.charAt(currentCharIndex + 1) != ARRAY_VARIABLE_END_CHARACTER ||
						!Character.isJavaIdentifierPart(content.charAt(currentCharIndex - 1)))) { 
					previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, currentCharIndex, content);
					currentEnumeratedContext = ParsingContextType.PhpDynamicText;
				} else if (content.charAt(currentCharIndex) == START_OF_LINE_COMMENT_CONTEXT.charAt(0) && 
						content.charAt(currentCharIndex + 1) == START_OF_LINE_COMMENT_CONTEXT.charAt(1)) {
					previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, currentCharIndex, content);
					currentEnumeratedContext = ParsingContextType.LineComment;
				} else if (content.charAt(currentCharIndex) == START_OF_COMMENT_CONTEXT.charAt(0) && 
						content.charAt(currentCharIndex + 1) == START_OF_COMMENT_CONTEXT.charAt(1)) {
					previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, currentCharIndex, content);
					currentEnumeratedContext = ParsingContextType.Comment;
				}
			} else if (currentEnumeratedContext == ParsingContextType.Text) {
				if (content.charAt(currentCharIndex) == START_OF_PHP_CONTEXT.charAt(0)
						&& content.charAt(currentCharIndex + 1) == START_OF_PHP_CONTEXT.charAt(1)) {
					previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, currentCharIndex, content);
					currentEnumeratedContext = ParsingContextType.Php;
				}
			} else if (currentEnumeratedContext == ParsingContextType.PhpText) {
				if (content.charAt(currentCharIndex) == END_OF_TEXT_CONTEXT.charAt(0)) {
					if (content.charAt(currentCharIndex - 1) != ESCAPE_CHARACTER || 
							(content.charAt(currentCharIndex - 2) == ESCAPE_CHARACTER)) {
						previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, ++currentCharIndex, content);
						currentEnumeratedContext = ParsingContextType.Php;
					}
				} 
			} else if (currentEnumeratedContext == ParsingContextType.PhpDynamicText) {
				if (content.charAt(currentCharIndex) == END_OF_DYNAMIC_TEXT_CONTEXT.charAt(0)) {
					if (content.charAt(currentCharIndex - 1) != ESCAPE_CHARACTER || 
							(content.charAt(currentCharIndex - 2) == ESCAPE_CHARACTER)) {
						previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, ++currentCharIndex, content);
						currentEnumeratedContext = ParsingContextType.Php;
					}
				}
			} else if (currentEnumeratedContext == ParsingContextType.LineComment) {
				if (content.charAt(currentCharIndex) == END_OF_LINE_COMMENT_CONTEXT.charAt(0)) {
					previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, currentCharIndex + 1, content);
					currentEnumeratedContext = ParsingContextType.Php;
				}
			} else if (currentEnumeratedContext == ParsingContextType.Comment) {
				if (content.charAt(currentCharIndex) == END_OF_COMMENT_CONTEXT.charAt(0)) {
					currentCharIndex++;
					if (content.charAt(currentCharIndex) == END_OF_COMMENT_CONTEXT.charAt(1)) {
						previousBeginIndex = updateContextList(currentEnumeratedContext, previousBeginIndex, currentCharIndex + 1, content);
						currentEnumeratedContext = ParsingContextType.Php;						
					}
				}
			}
		}
		return fileContextContentList;
	}
	
	private int updateContextList (ParsingContextType context, int beginIndex, int endIndex, String content) {
		fileContextContentList.add(new ParsingContext(context, beginIndex, endIndex, content));
		return endIndex;
	}
}
