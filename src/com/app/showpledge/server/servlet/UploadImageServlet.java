package com.app.showpledge.server.servlet;

import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.BlobstoreFileItemFactory.BlobstoreFileItem;
import gwtupload.server.gae.BlobstoreUploadAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.showpledge.server.AbstractRemoteServlet;
import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.shared.entities.Show;
import com.app.showpledge.shared.entities.User;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

/**
 * https://gist.github.com/767826
 * 
 * @author mjdowell
 * 
 */
@SuppressWarnings("serial")
public class UploadImageServlet extends BlobstoreUploadAction {

	private static final Logger LOG = LoggerFactory.getLogger(UploadImageServlet.class);

	private ImagesService imagesService = ImagesServiceFactory.getImagesService();

	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {

		if (sessionFiles.size() == 1 && sessionFiles.get(0) instanceof BlobstoreFileItem) {
			
			BlobstoreFileItem blob = (BlobstoreFileItem) sessionFiles.get(0);

			msg("Getting keys");
			BlobKey blobKey = blob.getKey();

			msg("Getting image url");
			String imageUrl = imagesService.getServingUrl(blobKey);
			msg("Image URL: " + imageUrl);

			msg("Getting user");
			User user = (User) request.getSession().getAttribute(AbstractRemoteServlet.USER_SESSION_KEY);
			msg("Got user: " + user);
			// TODO: USER CHECK HERE

			/*
			 * Note: Currently App Engine does not support uploading form data combined with multi-part image data so we have to store the
			 * Show in the sesion on a previous screen so we know what show to associate it with.
			 */

			Show show = AbstractRemoteServlet.getShowInSession(request);
			msg("Show: " + show);

			show.setPrimaryBlobKey(blobKey.getKeyString());
			show.setPrimaryImageUrl(imageUrl);
			updateShow(show);
			
			msg("Blob Key: " + blobKey.getKeyString());
			msg("Image URL: " + imageUrl);
			return imageUrl;
			
		} else {
			// Cleanup any blobs
			for (FileItem item : sessionFiles) {
				item.delete();
			}
			throw new UploadActionException("Illegal upload format");
		}
	}

	private void msg(String in) {
		System.out.println(in);
		LOG.debug(in);
	}

	private void updateShow(Show inShow) {
		Obj.begin().put(inShow);
	}
}
