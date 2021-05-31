var EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.MascaraCPF = (function() {

	function MascaraCPF() {
		this.inputCpf = $('.js-cpf');
	}
	
	MascaraCPF.prototype.iniciar = function() {
		this.inputCpf.mask('000.000.000-00');
	}
		
	return MascaraCPF;	
}());

EstudoEstrategico.Security = (function() {
	
	function Security() {
		this.token = $('input[name=_csrf').val();
		this.header = $('input[name=_csrf_header]').val();
	}
	
	Security.prototype.enable = function() {
		$(document).ajaxSend(function(event, jqxhr, settings){
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
	}
	
	return Security;
}());

EstudoEstrategico.MascaraNumeros = (function(){
	function MascaraNumeros() {		
		this.numero = $('.js-numero');
	}
	
	MascaraNumeros.prototype.habilitar = function() {
		this.numero.mask('#0', {reverse: true});
	}
	
	return MascaraNumeros;
})();

EstudoEstrategico.DropdownItemKeepOpen = (function(){
	function DropdownItemKeepOpen() {		
		this.dropdownItemKeepOpen = $('.dropdown-item.keep-open')
	}
	
	DropdownItemKeepOpen.prototype.habilitar = function() {
		this.dropdownItemKeepOpen.on('click', onDropdownItemKeepOpenClicked.bind(this));
	}
	
	function onDropdownItemKeepOpenClicked(e) {
		e.stopPropagation();
	}
	
	return DropdownItemKeepOpen;
})();

EstudoEstrategico.Tema = (function(){
	function Tema() {		
		this.modoEscuro = $('#modo-escuro');
		this.url = this.modoEscuro.data('url');
		this.modoEscuroStyleSheet = $('#modoEscuroStyleSheet');
		this.pcodedNavbar = $('.pcoded-navbar');
	}
	
	Tema.prototype.iniciar = function() {
		this.modoEscuro.on('change', onModoEscuroChanged.bind(this));
	}
	
	function onModoEscuroChanged() {
		$.ajax({
			url: this.url + '?modoEscuro='+this.modoEscuro.is(':checked'),
			method: 'PUT',
			success: onTemaAlteradoComSucesso.bind(this),
			error: onErroAlteracao.bind(this)
		});
	}
	
	function onTemaAlteradoComSucesso(navbarClass) {
		this.modoEscuroStyleSheet.prop("disabled", !this.modoEscuro.is(':checked'));
		this.pcodedNavbar.removeClass().addClass(navbarClass);
		
	}
	
	function onErroAlteracao(e) {
		swal('Oops!', e.responseText, 'error');
	}
	
	return Tema;
})();

$(function(){	
	var mascaraCPF = new EstudoEstrategico.MascaraCPF();
	mascaraCPF.iniciar();
	
	var security = new EstudoEstrategico.Security();
	security.enable();
	
	var mascaraNumeros = new EstudoEstrategico.MascaraNumeros();
	mascaraNumeros.habilitar();
	
	var dropdownItemKeepOpen = new EstudoEstrategico.DropdownItemKeepOpen();
	dropdownItemKeepOpen.habilitar();
	
	var tema = new EstudoEstrategico.Tema();
	tema.iniciar();
});