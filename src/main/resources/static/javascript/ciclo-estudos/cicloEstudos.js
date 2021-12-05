var EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.CicloEstudos = (function() {
	function CicloEstudos() {
		this.inputNome = $('input[name=nome]');
		this.uuid = $('#uuid').val();
	}
	
	CicloEstudos.prototype.iniciar = function() {
		this.inputNome.on('blur', onInputNomeBlur.bind(this));
	}
	
	function onInputNomeBlur() {
		$.ajax({
			url: 'atualizar-nome?uuid=' + this.uuid,
			contentType: 'application/x-www-form-urlencoded',
			method: 'PUT',
			data : {nome: this.inputNome.val()}
		});
	}
	
	return CicloEstudos;
}());

$(function() {
	var cicloEstudos = new EstudoEstrategico.CicloEstudos();
	cicloEstudos.iniciar();
});