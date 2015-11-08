package com.funfactory.cangamemake.db;

/**
 * Enum to provide the tables names.
 */
public enum Table {

	/**
	 * The system tables
	 */
	PACIENTE("tb_paciente"), 
	RANKING("tb_ranking"), 
	CONFIG("tb_config"),
	PECS("tb_pecs"),
	ROTINA("tb_rotina"),
	ROTINA_PECS("tb_rotina_pecs"),
	CATEGORIA("tb_categoria");

	/**
	 * The name.
	 */
	private String name;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            of the database table
	 */
	private Table(final String name) {
		this.name = name;
	}

	/**
	 * Get the name of the database table.
	 * 
	 * @return table name
	 */
	public String getName() {
		return name;
	}
}