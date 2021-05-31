package br.com.anthonini.estudoEstrategico.model;

public enum Tema {
	CLARO("pcoded-navbar menu-light"),
	ESCURO("pcoded-navbar navbar-dark brand-dark");
	
	private String navbarClass;
	
	private Tema(String navbarClass) {
		this.navbarClass = navbarClass;
	}

	public String getNavbarClass() {
		return navbarClass;
	}
}
