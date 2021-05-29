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

$(function(){	
	var mascaraCPF = new EstudoEstrategico.MascaraCPF();
	mascaraCPF.iniciar();
	
	var security = new EstudoEstrategico.Security();
	security.enable();
	
	var mascaraNumeros = new EstudoEstrategico.MascaraNumeros();
	mascaraNumeros.habilitar();
});