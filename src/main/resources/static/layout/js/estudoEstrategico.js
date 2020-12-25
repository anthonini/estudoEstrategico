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

$(function(){	
	var mascaraCPF = new EstudoEstrategico.MascaraCPF();
	mascaraCPF.iniciar();
});