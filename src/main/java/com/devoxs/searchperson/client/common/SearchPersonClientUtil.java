package com.devoxs.searchperson.client.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.devoxs.searchperson.client.domain.ContactInfo;
import com.devoxs.searchperson.client.domain.ContactValue;

public final class SearchPersonClientUtil {

	private SearchPersonClientUtil() {

	}

	public static String findPhoneNumber(List<ContactInfo> contactInfo) {

		return findValueFromSource(contactInfo, SearchPersonClientContants.MOVIL_PHONE);
		
	}
	
	
	public static String findEmail(List<ContactInfo> contactInfo) {

		return findValueFromSource(contactInfo, SearchPersonClientContants.EMAIL);

	}

	private static String findValueFromSource(List<ContactInfo> contactInfo, String kind ) {
		if (CollectionUtils.isEmpty(contactInfo)) {
			return null;
		}

		List<ContactInfo> filterbyKind = filterByKind(contactInfo, kind);
		if (CollectionUtils.isEmpty(filterbyKind)) {
			return null;
		}

		List<ContactValue> values = filterbyKind.iterator().next().getValues();

		if (CollectionUtils.isEmpty(values)) {
			return null;
		}

		return values.iterator().next().getValue();
	}
	
	
	private static List<ContactInfo> filterByKind(List<ContactInfo> contactInfo, String kind) {
		return contactInfo.stream().filter(c -> kind.equals(c.getKind())).collect(Collectors.toList());
	}
}
