var EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.DisciplinaPeriodo = (function() {
	function DisciplinaPeriodo() {
		this.disciplinaPeriodoModal = $('#disciplinaPeriodoModal');
		this.uuid = $('#uuid').val();
	}
	
	DisciplinaPeriodo.prototype.iniciar = function() {
		this.disciplinaPeriodoModal.on('show.bs.modal', onShowDisciplinaPeriodoModal.bind(this));
		this.disciplinaPeriodoModal.on('disciplinas-atualizadas', onDisciplinasAtualizadas.bind(this));
	}
	
	function onShowDisciplinaPeriodoModal(event) {
		var dia = $(event.relatedTarget).data('dia');
		var url = this.disciplinaPeriodoModal.data('url') + '?uuid=' + this.uuid + '&dia=' + dia;
		var response = $.ajax({
			url: url,
			contentType: 'application/json',
			method: 'PUT'
		});
		
		response.done(onDisciplinaPeriodoModalResponse.bind(this));
	}
	
	function onDisciplinaPeriodoModalResponse(html) {
		this.disciplinaPeriodoModal.html(html);
	}
	
	function onDisciplinasAtualizadas(event, dia) {		
		var response = $.ajax({
			url: 'disciplinas-periodo?uuid=' + this.uuid + '&dia=' + dia,
			contentType: 'application/json',
			method: 'GET'
		});
		
		response.done(onDisciplinasAtualizadasResponse.bind(this, dia));
	}

	function onDisciplinasAtualizadasResponse(dia, html) {
		$('#disciplinas-' + dia.toLowerCase() + '-dia').html(html);
	}
	
	function onRemoverDisciplinaPeriodoBtnClicked(event) {
		event.preventDefault();
		var btn = $(event.currentTarget);
		var url = btn.data('url');
		var disciplinaPeriodo = btn.data('disciplinaPeriodo');
		
		swal({
			title: 'Tem certeza?',
			text: 'Excluir a disciplinaPeriodo "' + disciplinaPeriodo + '"?',
			icon: 'warning',
			buttons: ['Cancelar', 
				{
					text: 'Sim, exclua agora!',
				    value: true,
				    visible: true,
				    closeModal: false
				}]
		}).then((remover) => {
		  if (remover) {
			  onRemoverConfirmado.call(this, url)
		  }
		});
	}
	
	function onRemoverConfirmado(url) {
		$.ajax({
			url: url + '?uuid=' + this.uuid,
			method: 'DELETE',
			success: onRemovidoComSucesso.bind(this),
			error: onErroRemocao.bind(this)
		});
	}
	
	function onRemovidoComSucesso() {
		$('#disciplinaPeriodoModal').trigger('disciplinaPeriodos-atualizadas');
		swal('DisciplinaPeriodo removida com sucesso!', '', 'success');
	}
	function onErroRemocao(e) {
		swal('Oops!', e.responseText, 'error');
	}
	
	return DisciplinaPeriodo;
}());

$(function() {
	var disciplinaPeriodo = new EstudoEstrategico.DisciplinaPeriodo();
	disciplinaPeriodo.iniciar();
});