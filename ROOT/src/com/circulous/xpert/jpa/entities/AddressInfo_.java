package com.circulous.xpert.jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-28T20:45:00.131+0530")
@StaticMetamodel(AddressInfo.class)
public class AddressInfo_ {
	public static volatile SingularAttribute<AddressInfo, Integer> addressId;
	public static volatile SingularAttribute<AddressInfo, String> addressLine1;
	public static volatile SingularAttribute<AddressInfo, String> addressLine2;
	public static volatile SingularAttribute<AddressInfo, String> postalCode;
	public static volatile SingularAttribute<AddressInfo, String> contactName;
	public static volatile SingularAttribute<AddressInfo, String> contactPhone;
	public static volatile SingularAttribute<AddressInfo, String> description;
	public static volatile SingularAttribute<AddressInfo, String> email;
	public static volatile SingularAttribute<AddressInfo, Character> isActive;
	public static volatile SingularAttribute<AddressInfo, Character> isDeleted;
	public static volatile SingularAttribute<AddressInfo, String> createdUser;
	public static volatile SingularAttribute<AddressInfo, Date> createdDate;
	public static volatile SingularAttribute<AddressInfo, String> updatedUser;
	public static volatile SingularAttribute<AddressInfo, Date> updatedDate;
	public static volatile SingularAttribute<AddressInfo, Integer> cityId;
	public static volatile CollectionAttribute<AddressInfo, VenueInfo> venueInfoCollection;
	public static volatile CollectionAttribute<AddressInfo, CustomerInfo> customerInfoCollection;
	public static volatile CollectionAttribute<AddressInfo, ServiceProviderInfo> serviceProviderInfoCollection;
	public static volatile SingularAttribute<AddressInfo, String> latitude;
	public static volatile SingularAttribute<AddressInfo, String> longitude;
}
