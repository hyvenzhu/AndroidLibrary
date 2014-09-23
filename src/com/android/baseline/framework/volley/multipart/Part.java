package com.android.baseline.framework.volley.multipart;
import java.io.IOException;
import java.io.OutputStream;
/**
 * A part interface defines part length and function to write content
 * @author hiphonezhu@gmail.com
 * @version [BaseLine_Android_V5, 2014-9-22]
 */
public interface Part
{
    public long getContentLength(Boundary boundary);
    public void writeTo(final OutputStream out, Boundary boundary) throws IOException;
}
