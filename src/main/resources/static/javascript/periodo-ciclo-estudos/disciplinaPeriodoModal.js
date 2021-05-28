var EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.DisciplinaPeriodoModal = (function() {
	function DisciplinaPeriodoModal() {
		this.modal = $('#disciplinaPeriodoModal');
		this.form = $('form[name="form-disciplina-periodo"]');		
		this.url = this.form.attr('action');
		this.uuid = $('#uuid').val();
		this.dia = $('#dia').val();
	}
	
	DisciplinaPeriodoModal.prototype.iniciar = function() {
		this.form.submit(onFormSubmitted.bind(this));
	}
	
	function onFormSubmitted(event) {
		event.preventDefault();
		var response = $.ajax({
			url: this.url + '?uuid=' + this.uuid + '&dia=' + this.dia,
			method: 'POST',
	        contentType: 'application/x-www-form-urlencoded',
	        data : this.form.serialize()
		 });
		
		response.done(onFormSubmittedResponse.bind(this));
	}
	
	function onFormSubmittedResponse(html) {
		this.modal.html(html);
	}
	
	return DisciplinaPeriodoModal;
}());

$(function() {
	var disciplinaPeriodoModal = new EstudoEstrategico.DisciplinaPeriodoModal();
	disciplinaPeriodoModal.iniciar();
});