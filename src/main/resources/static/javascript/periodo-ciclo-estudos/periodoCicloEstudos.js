var EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.PeriodoCicloEstudos = (function() {
	function PeriodoCicloEstudos() {
		this.inputDuracao = $('input[name=duracao]');
		this.uuid = $('#uuid').val();
	}
	
	PeriodoCicloEstudos.prototype.iniciar = function() {
		this.inputDuracao.on('blur', onInputDuracaoBlur.bind(this));
	}
	
	function onInputDuracaoBlur() {
		$.ajax({
			url: 'atualizar-duracao?uuid=' + this.uuid,
			contentType: 'application/x-www-form-urlencoded',
			method: 'PUT',
			data : {duracao: this.inputDuracao.val()}
		});
	}
	
	return PeriodoCicloEstudos;
}());

$(function() {
	var periodoCicloEstudos = new EstudoEstrategico.PeriodoCicloEstudos();
	periodoCicloEstudos.iniciar();
});