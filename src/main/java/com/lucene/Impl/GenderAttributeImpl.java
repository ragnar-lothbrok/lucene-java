package com.lucene.Impl;

import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

import com.lucene.api.GenderAttribute;

public class GenderAttributeImpl extends AttributeImpl implements GenderAttribute {

	private Gender gender = Gender.Undefined;

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Gender getGender() {
		return gender;
	};

	@Override
	public void clear() {
		gender = Gender.Undefined;
	}

	@Override
	public void copyTo(AttributeImpl target) {
		((GenderAttribute) target).setGender(gender);
	}

	@Override
	public void reflectWith(AttributeReflector reflector) {
		reflector.reflect(GenderAttribute.class, "gender", gender);
	}
}
