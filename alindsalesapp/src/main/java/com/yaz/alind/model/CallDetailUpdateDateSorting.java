package com.yaz.alind.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * For sorting of CallDetail ->  UpdatedAt
 *   
 * @author Libu
 *
 */

public class CallDetailUpdateDateSorting implements Serializable,Comparator<CallDetail> {

	@Override
	public int compare(CallDetail o1, CallDetail o2) {
		return  o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
	}
	

}
