/* pl.exso.core.mat.domain.model.beans.FilmBean (2013-11-27)
 * 
 * FilmBean.java
 * 
 * Copyright 2013 EXPERT SOLUTIONS Sp. z o. o.
 */
package pl.exso.core.mat.domain.model.beans;

import org.agiso.core.domain.beans.BaseEntity;

import pl.exso.core.mat.domain.model.Film;

/**
 * 
 * 
 * @author <a href="mailto:inazwisko@araj.pl">MATEUSZ Kołdowski</a>
 */
public class FilmBean extends BaseEntity<Long> implements Film {
	private static final long serialVersionUID = 1L;

//	Definicje pól ------------------------------------------------------------
	protected String name;
	protected int year;
	//#@_field_@#

//	Konstruktor --------------------------------------------------------------
	public FilmBean() {
		initialize();
	}
	public FilmBean(final long id) {
		initialize();

		this.id = id;
	}
	public FilmBean(final Film film) {
		initialize();

		if(film instanceof FilmBean) {
			final FilmBean bean = (FilmBean)film;

			this.id = bean.id;

			this.name = bean.name;
			this.year = bean.year;
			//#@_constuctor_instance_@#
		} else {
			this.id = film.getId();

			this.name = film.getName();
			this.year = film.getYear();
			//#@_constctor_else_@#
		}
	}
	protected void initialize() {
	}

//	Definicje metod dostępowych ----------------------------------------------
	@SuppressWarnings("unchecked")
	public <T extends FilmBean> T withId(Long id) {
		this.id = id;
		return (T)this;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@SuppressWarnings("unchecked")
	public <T extends FilmBean> T withName(String name) {
		this.name = name;
		return (T)this;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	@SuppressWarnings("unchecked")
	public <T extends FilmBean> T withYear(int year) {
		this.year = year;
		return (T)this;
	}
	//#@_accessors_@#
}