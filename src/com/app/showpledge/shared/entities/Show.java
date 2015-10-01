package com.app.showpledge.shared.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.app.showpledge.shared.entities.iface.EntityIface;
import com.app.showpledge.shared.entities.iface.Searchable;
import com.google.gwt.view.client.ProvidesKey;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

@Entity
@Unindexed
@Cached
public class Show implements EntityIface, Searchable {

	private static final long serialVersionUID = 2893712114305659554L;

	/**
	 * Various public show statuses
	 * 
	 * @author mjdowell
	 * 
	 */
	public static enum ShowStatus {
		ON_AIR(0), IN_TROUBLE(1), GOING_TO_BE_CANCELLED(2), CANCELLED(3), OFF_AIR(4), ARCHIVED(5);

		int index;

		ShowStatus(int in) {
			index = in;
		}

		public static ShowStatus getFromIndex(int inIndex) {
			for (ShowStatus s : ShowStatus.values()) {
				if (s.index == inIndex) {
					return s;
				}
			}
			return null;
		}

		public int getIndex() {
			return index;
		}
	}

	/**
	 * This is the status of this "Show" in relation to our system
	 * 
	 * @author mjdowell
	 * 
	 */
	public static enum SystemStatus {
		NOMINATED(0), ACCEPTED(1), REJECTED(2);

		int index;

		SystemStatus(int in) {
			index = in;
		}

		public static SystemStatus getFromIndex(int inIndex) {
			for (SystemStatus s : SystemStatus.values()) {
				if (s.index == inIndex) {
					return s;
				}
			}
			return null;
		}

		public int getIndex() {
			return index;
		}
	}

	@Id
	private Long id;

	@Indexed
	private String name = null;

	private Key<User> nominationUser = null;

	private Key<User> promotionUser = null;

	private String showStartDate = null;

	private Date nominationDate = null;

	private Date acceptedDate = null;

	private String corporateUrl = null;

	private String imdbUrl = null;

	private String producers = null;
	
	private String description = null;

	@Indexed
	private ShowStatus showStatus = null;

	@Indexed
	private SystemStatus systemStatus = null;

	// We should possibly be storing the URL's that are generated from these
	// keys. Faster?
	private List<String> blobKeys = null;

	private List<String> imageUrls = null;

	private String primaryBlobKey = null;
	private String primaryImageUrl = null;

	// The below code converts the List of blob keys to a single String
	void importOldBlobKeys(@AlsoLoad("blobKeys") List<String> blobKeys) {
		if (blobKeys != null && !blobKeys.isEmpty()) {
			primaryBlobKey = blobKeys.iterator().next();
		}
	}

	// The below code converts the List of blob keys to a single String
	void importOldImageUrls(@AlsoLoad("imageUrls") List<String> urls) {
		if (urls != null && !urls.isEmpty()) {
			primaryImageUrl = urls.iterator().next();
		}
	}

	@Transient
	private Key<Show> key;

	@Transient
	private User campaignManager = null;

	private double pledgeTotal = 0.0;

	// FULL TEXT SEARCH
	@Indexed
	private Set<String> fts = null;

	@Indexed
	private boolean promoted;

	public Show() {
		super();
	}

	@PostLoad
	void processPrimaryImageURL() {
		setPrimaryImageUrl(replaceLocalIP(getPrimaryImageUrl()));
	}

	private String replaceLocalIP(String inUrl) {
		if (inUrl != null) {
			return inUrl.replace(new StringBuffer("0.0.0.0"), new StringBuffer("127.0.0.1"));
		} else {
			return null;
		}
	}

	/**
	 * This is used on client side data tables
	 */
	public static final ProvidesKey<Show> KEY_PROVIDER = new ProvidesKey<Show>() {
		public Object getKey(Show item) {
			return item == null ? null : item.getId();
		}
	};

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrimaryBlobKey() {
		return primaryBlobKey;
	}

	public void setPrimaryBlobKey(String primaryBlobKey) {
		this.primaryBlobKey = primaryBlobKey;
	}

	public boolean isPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	public User getCampaignManager() {
		return campaignManager;
	}

	public void setCampaignManager(User campaignManager) {
		this.campaignManager = campaignManager;
	}

	public Set<String> getFts() {
		return fts;
	}

	public void setFts(Set<String> fts) {
		this.fts = fts;
	}

	public void setKey(Key<Show> key) {
		this.key = key;
	}

	public Key<Show> getKey() {
		return key;
	}

	public String getPrimaryImageUrl() {
		return primaryImageUrl;
	}

	public void setPrimaryImageUrl(String primaryImageUrl) {
		this.primaryImageUrl = primaryImageUrl;
	}

	public double getPledgeTotal() {
		return pledgeTotal;
	}

	public void setPledgeTotal(double pledgeTotal) {
		this.pledgeTotal = pledgeTotal;
	}

	@Deprecated
	public List<String> getImageUrls() {
		return imageUrls;
	}

	@Deprecated
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	@Deprecated
	public void addImageUrl(String imageUrl) {
		setPrimaryImageUrl(imageUrl);
	}

	@Deprecated
	public List<String> getBlobKeys() {
		return blobKeys;
	}

	@Deprecated
	public void setBlobKeys(List<String> blobKeys) {
		this.blobKeys = blobKeys;
	}

	@Deprecated
	public void addBlobKey(String inKey) {
		setPrimaryBlobKey(inKey);
	}

	public Key<User> getNominationUser() {
		return nominationUser;
	}

	public void setNominationUser(Key<User> nominationUser) {
		this.nominationUser = nominationUser;
	}

	public Key<User> getPromotionUser() {
		return promotionUser;
	}

	public void setPromotionUser(Key<User> promotionUser) {
		this.promotionUser = promotionUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShowStartDate() {
		return showStartDate;
	}

	public void setShowStartDate(String showStartDate) {
		this.showStartDate = showStartDate;
	}

	public Date getNominationDate() {
		return nominationDate;
	}

	public void setNominationDate(Date nominationDate) {
		this.nominationDate = nominationDate;
	}

	public Date getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public String getCorporateUrl() {
		return corporateUrl;
	}

	public void setCorporateUrl(String corporateUrl) {
		this.corporateUrl = corporateUrl;
	}

	public String getImdbUrl() {
		return imdbUrl;
	}

	public void setImdbUrl(String imdbUrl) {
		this.imdbUrl = imdbUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProducers() {
		return producers;
	}

	public void setProducers(String producers) {
		this.producers = producers;
	}

	public ShowStatus getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(ShowStatus status) {
		this.showStatus = status;
	}

	public SystemStatus getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(SystemStatus systemStatus) {
		this.systemStatus = systemStatus;
	}

	@Override
	public String getSearchableContent() {
		return getName().toLowerCase() + " " + getDescription().toLowerCase();
	}

}
