package com.funfactory.cangamemake.model.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.funfactory.cangamemake.db.Table;

/**
 * BaseEntity abstract class.
 */
public abstract class AbstractBaseEntity implements Serializable {

	/**
	 * The Constant INIT_PRIME.
	 */
	private static final int INIT_PRIME = 31;

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The Constant ID_COLUMN.
	 */
	public static final String ID_COLUMN = "id";

	/**
	 * The id entity.
	 */
	private Long idEntity;

	/**
	 * The result.
	 */
	private boolean result;

	/**
	 * Gets the child table.
	 *
	 * @return the child table
	 */
	public Map<Table, String> getChildTable() {
		return new HashMap<Table, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int prime;
		prime = INIT_PRIME;
		int result;
		result = prime * 1 + ((idEntity == null) ? 0 : idEntity.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		result = true;

		result = commonSituations(obj);
		if (obj != null && getClass() != obj.getClass()) {
			result = false;
		}
		final AbstractBaseEntity other = (AbstractBaseEntity) obj;
		if (idEntity == null) {
			if (other != null && other.idEntity != null) {
				result = false;
			}
		} else if (other != null && !idEntity.equals(other.idEntity)) {
			result = false;
		}
		return result;
	}

	/**
	 * Common situations.
	 *
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 */
	private boolean commonSituations(final Object obj) {
		if (this == obj) {
			result = true;
		}
		if (obj == null) {
			result = false;
		}

		return result;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getIdEntity() {
		return idEntity;
	}

	/**
	 * Sets the id.
	 *
	 * @param idEntity
	 *            the new id
	 */
	public void setIdEntity(final Long idEntity) {
		this.idEntity = idEntity;
	}

}
