EstudoEstrategico = EstudoEstrategico || {};

EstudoEstrategico.DialogoExclusao = (function(){
	
	function DialogoExclusao() {
		this.exclusaoBtn = $('.js-exclusao-btn');
		if(window.location.search.indexOf('excluido') > -1){			
			swal('Pronto!', 'Excluído com sucesso!', 'success');
		}
	}
	
	DialogoExclusao.prototype.start = function() {
		this.exclusaoBtn.on('click', onExclusaoBtnClicked.bind(this));
	}
	
	function onExclusaoBtnClicked(event) {
		event.preventDefault();
		var clickedBtn = $(event.currentTarget);
		var url = clickedBtn.data('url');
		var objeto = clickedBtn.data('objeto');
		
		swal({
			title: 'Tem certeza?',
			text: 'Excluir "' + objeto + '"? Você não poderá recuperar depois.',
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
			  onExlusaoConfirmada.call(this, url)
		  }
		});
	}
	
	function onExlusaoConfirmada(url) {
		$.ajax({
			url: url,
			method: 'DELETE',
			success: onExclusaoSuccess.bind(this),
			error: onExclusaoError.bind(this)
		});
	}
	
	function onExclusaoSuccess() {
		var urlAtual = window.location.href;
		var separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
		var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual + separador + 'excluido';
		
		window.location = novaUrl;
	}
	function onExclusaoError(e) {
		swal('Oops!', e.responseText, 'error');
	}
	
	return DialogoExclusao;
}());

$(function(){
	var dialogoExclusao = new EstudoEstrategico.DialogoExclusao();
	dialogoExclusao.start();
});