var EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.Estudo = (function() {
	function Estudo() {
		this.inputDisciplinaDiaEstudo = $('.js-disciplina-dia-estudo');
		this.inputRevisao = $('.js-revisao');
		this.cargaHoraria = $('input:text[value=""][name="cargaHorariaLiquida"]').first().parent().parent().find('.js-ch-aula').text()*60*1000 + 5*60*1000 - 15*60*1000;
		this.horaAcesso = (new Date()).getTime();
		this.navBar = $('nav.pcoded-navbar');
	}
	
	Estudo.prototype.iniciar = function() {
		this.inputDisciplinaDiaEstudo.on('blur', onInputDadoEstudoBlur.bind(this));
		this.inputRevisao.on('blur', onInputRevisaoBlur.bind(this));
		keepAliveTimer.call(this);
		this.navBar.addClass('navbar-collapsed');
	}
	
	function onInputDadoEstudoBlur(event) {
		var id = $(event.target).data('id');
		var inputs = $('input[data-id="'+id+'"].js-disciplina-dia-estudo');
		
		var response = $.ajax({
			url: 'atualizar-estudos-disciplina?id=' + id,
			contentType: 'application/x-www-form-urlencoded',
			method: 'PUT',
			data : inputs.serialize()
		});
		
		response.done(onEstudoDisciplinaAtualizadaResponse.bind(this, id));
	}
	
	function onEstudoDisciplinaAtualizadaResponse(id, disciplinaDiaEstudo) {
		$('#porcentagem_'+id).html(disciplinaDiaEstudo.porcentagemAcertoQuestoes);
	}
	
	function onInputRevisaoBlur(event) {
		var id = $(event.target).data('id');
		var inputs = $('input[data-id="'+id+'"].js-revisao');
		
		var response = $.ajax({
			url: 'atualizar-estudos-revisao?id=' + id,
			contentType: 'application/x-www-form-urlencoded',
			method: 'PUT',
			data : inputs.serialize()
		});
		
		response.done(onRevisaoAtualizadaResponse.bind(this, id));
	}
	
	function onRevisaoAtualizadaResponse(id, revisao) {
		$('#porcentagem-revisao_'+id).html(revisao.porcentagemAcertoQuestoes);
	}
	
	function keepAliveTimer() {
		this.keepAliveInterval = setInterval(keepAlive.bind(this), 5*60*1000);
	}
	
	function keepAlive() {
		$.ajax({
			url: 'keep-alive',
			method: 'GET'
		});
		
		if((new Date()).getTime() - this.horaAcesso >= this.cargaHoraria) {
			clearInterval(this.keepAliveInterval);
		}
	}
	
	return Estudo;
}());

$(function() {
	var estudo = new EstudoEstrategico.Estudo();
	estudo.iniciar();
});