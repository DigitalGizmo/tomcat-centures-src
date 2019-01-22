/**
 * Title:        ImageServlet<p>
 * Description:  
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ImageServlet.java,v 1.7 2002/04/12 14:08:12 pbrown Exp $
 *
 * $Log: ImageServlet.java,v $
 * Revision 1.7  2002/04/12 14:08:12  pbrown
 * fixed copyright info
 *
 * Revision 1.6  2001/05/24 14:04:56  pbrown
 * added mrsid root member to httpdbjspbase...no more init servlet
 *
 * Revision 1.5  2001/02/28 16:09:59  pbrown
 * fixes link for navigator view
 *
 * Revision 1.4  2000/11/21 21:23:03  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.3  2000/10/24 20:08:32  pbrown
 * fixed site.root initialization, and changed rectangle color from blue to
 * red
 *
 * Revision 1.2  2000/09/25 18:17:40  pbrown
 * fixed positioning of rectangle
 *
 * Revision 1.1  2000/09/24 04:24:41  pbrown
 * servlet to draw rectangle in image navigator
 *
 */
package edu.umass.ccbit.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.URL;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import edu.umass.ckc.servlet.ServletParams;
import edu.umass.ckc.servlet.ServletParser;
import edu.umass.ckc.servlet.InitParams;
import edu.umass.ckc.util.CkcException;
import java.awt.Color;
import java.awt.Rectangle;
import edu.umass.ccbit.image.MrSidImage;
import java.net.URLDecoder;
import edu.umass.ccbit.jsp.HttpDbJspBase;

public class ImageServlet extends HttpServlet
{
  protected ServletParams servletParams_;
  protected InitParams initParams_;
  
  // parameters
  public static final String ImageSrc_="imgsrc";
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    initParams_=new InitParams();
    initParams_.storeParameters(getServletConfig());
    servletParams_=new ServletParams();

    try
    {
      ServletParser parser=new ServletParser();
      parser.parse(request, servletParams_);
    }
    catch (Exception e)
    {
      
    }

    // TODO: fix this;
    String imgsrc=servletParams_.getString("imgsrc", "");
    try
    {
      imgsrc=URLDecoder.decode(imgsrc);
    }
    catch (Exception ex)
    {
    }
    int rx=servletParams_.getInt("rx", 0);
    int ry=servletParams_.getInt("ry", 0);
    int hz=servletParams_.getInt("rh", 0);
    int vt=servletParams_.getInt("rv", 0);

    URL url=new URL(imgsrc);
    InputStream istream=url.openStream();
    JPEGImageDecoder jpegDecoder=JPEGCodec.createJPEGDecoder(istream);
    BufferedImage img=jpegDecoder.decodeAsBufferedImage();
    Graphics2D g;
    g=img.createGraphics();
    g.setColor(Color.red);
    g.draw(new Rectangle(rx, ry, hz, vt));

    g.drawImage(img, 0, 0, null);
    ServletOutputStream  out = response.getOutputStream ( );
    JPEGImageEncoder jpegEncoder=JPEGCodec.createJPEGEncoder(out);
    response.setContentType("image/jpeg");
    jpegEncoder.encode(img);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    doGet(request, response);
  }
}
