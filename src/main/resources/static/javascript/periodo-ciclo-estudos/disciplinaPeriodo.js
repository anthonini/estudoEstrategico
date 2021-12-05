var EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.DisciplinaPeriodo = (function() {
	function DisciplinaPeriodo() {
		this.disciplinaPeriodoModal = $('#disciplinaPeriodoModal');
		this.uuid = $('#uuid').val();
	}
	
	DisciplinaPeriodo.prototype.iniciar = function() {
		this.disciplinaPeriodoModal.on('show.bs.modal', onShowDisciplinaPeriodoModal.bind(this));
		this.disciplinaPeriodoModal.on('disciplinas-atualizadas', onDisciplinasAtualizadas.bind(this));
		bindBtn.call(this);
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
		bindBtn.call(this);
	}
	
	function bindBtn() {
		this.removerDisciplinaBtn = $('.js-remover-disciplina');
		this.removerDisciplinaBtn.off('click');
		this.removerDisciplinaBtn.on('click', onRemoverDisciplinaBtnClicked.bind(this));
	}
	
	function onRemoverDisciplinaBtnClicked(event) {
		event.preventDefault();
		var btn = $(event.currentTarget);
		var url = btn.data('url');
		var disciplina = btn.data('disciplina');
		var dia = btn.data('dia');
		
		swal({
			title: 'Tem certeza?',
			text: 'Excluir a disciplina "' + disciplina + '"?',
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
			  onRemoverConfirmado.call(this, url, dia)
		  }
		});
	}
	
	function onRemoverConfirmado(url, dia) {
		$.ajax({
			url: url + '?uuid=' + this.uuid + '&dia=' + dia,
			method: 'DELETE',
			success: onRemovidoComSucesso.bind(this, dia),
			error: onErroRemocao.bind(this)
		});
	}
	
	function onRemovidoComSucesso(dia) {
		$('#disciplinaPeriodoModal').trigger('disciplinas-atualizadas', dia);
		swal('Disciplina removida com sucesso!', '', 'success');
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