/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
// A user Defined Exception class that can be thrown by the DNAParser Class only
public class EmptySourceException extends RuntimeException
{
	public EmptySourceException() { super(); } 
	public EmptySourceException(String str){ super(str); } 
}