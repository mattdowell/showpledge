package com.app.showpledge.client.util;

import com.app.showpledge.client.util.widget.WidgetFactory;
import com.app.showpledge.shared.entities.Show;
import com.google.gwt.user.client.ui.Image;

/**
 * Collection of client image utility methods
 * 
 * @author mjdowell
 * 
 */
public final class ImageUtil {
	
	private static final int THUMB_WIDTH_PIXELS = 100;
	private static final int MED_WIDTH_PIXELS = 250;
	
	/**
	 * Get a thumbnail
	 * @param inShow
	 * @return
	 */
	public static Image getPrimaryImageMed(Show inShow) {
		Image i = new Image(makeMed(inShow.getPrimaryImageUrl()));
		i.addClickHandler(WidgetFactory.buildViewShowDetailsHandler(inShow));
		return i;
	}	
	public static Image getImageMed(String inShowUrl) {
		return new Image(makeMed(inShowUrl));
	}
	
	
	/**
	 * Get a medium sized image that when you click on it, it 
	 * lets you edit the image.
	 * 
	 * @param inShow
	 * @return
	 */
	public static Image getImageMedEditable(Show inShow) {
		
		if (inShow.getPrimaryImageUrl() == null) {
			return null;
		}
		
		Image i = new Image(makeMed(inShow.getPrimaryImageUrl()));
		return i;
	}	

	/**
	 * Get a thumbnail
	 * @param inShow
	 * @return
	 */
	public static Image getPrimaryImageThumb(Show inShow) {
		
		if (inShow.getPrimaryImageUrl() == null) {
			return null;
		}
		
		Image i = new Image(makeThumb(inShow.getPrimaryImageUrl()));
		i.addClickHandler(WidgetFactory.buildViewShowDetailsHandler(inShow));
		return i;
	}
	


	/**
	 * Per docs:
	 * http://code.google.com/appengine/docs/java/javadoc/com/google/appengine
	 * /api/images/ImagesService.html#getServingUrl(com.google.appengine.api.
	 * blobstore.BlobKey,%20int,%20boolean)
	 * 
	 * @param inUrl
	 * @return
	 */
	public static String makeThumb(String inUrl) {
		return size(inUrl, THUMB_WIDTH_PIXELS);
	}
	public static String makeMed(String inUrl) {
		return size(inUrl, MED_WIDTH_PIXELS);
	}	
	
	private static String size(String inUrl, int inSize) {
		return inUrl + "=s" + inSize;
	}	

}
