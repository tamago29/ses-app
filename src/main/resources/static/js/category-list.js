document.addEventListener('DOMContentLoaded', function() {
	const btnEditMode = document.getElementById('btn-edit-mode');
    const btnCancel = document.getElementById('btn-cancel');
    const btnBack = document.getElementById('btn-back');
    const btnAdd = document.getElementById('btn-add');
    const btnSave = document.getElementById('btn-save');
    const categoryInputs = document.querySelectorAll('.category-input');
	const actionColumns = document.querySelectorAll('.action-column');
	
	// 編集モードに切り替える関数
    function enableEditMode() {
		// 入力欄を編集可能にする
		categoryInputs.forEach(input => {
			input.removeAttribute('readonly');
            input.classList.remove('readonly-mode');
        });
		
		// 削除列（操作ヘッダー＋全行の削除ボタンセル）を表示する
		actionColumns.forEach(col => col.classList.remove('is-hidden'));

        // ボタンの表示/非表示を切り替え
        btnEditMode.classList.add('is-hidden');
        btnBack.classList.add('is-hidden');
               
        btnAdd.classList.remove('is-hidden');
        btnSave.classList.remove('is-hidden');
        btnCancel.classList.remove('is-hidden');
	}

    // 閲覧モードに戻す関数
    function disableEditMode() {
		// 入力欄を読み取り専用に戻す
		categoryInputs.forEach(input => {
			input.setAttribute('readonly', 'readonly');
            input.classList.add('readonly-mode');
		});

		// 削除列を非表示にする
	    actionColumns.forEach(col => col.classList.add('is-hidden'));
				
        // ボタンの表示/非表示を元に戻す
        btnEditMode.classList.remove('is-hidden');
        btnBack.classList.remove('is-hidden');
                
        btnAdd.classList.add('is-hidden');
        btnSave.classList.add('is-hidden');
        btnCancel.classList.add('is-hidden');
	}

    // イベントリスナー設定
    btnEditMode.addEventListener('click', enableEditMode);
            
    btnCancel.addEventListener('click', function() {
	// キャンセル時は入力内容をリセットしてモード解除（必要に応じてページ再読み込み）
	disableEditMode();
    });
});