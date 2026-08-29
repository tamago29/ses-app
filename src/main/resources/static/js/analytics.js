document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('categoryChart');
    if (!canvas) return;

    // HTMLの data- 属性からラベルと値を取得（カンマ区切り文字列を配列に変換）
    const labels = canvas.dataset.labels ? canvas.dataset.labels.replace(/^\[|\]$/g, '').split(',').map(item => item.trim()) : [];
    const dataValues = canvas.dataset.values ? JSON.parse(canvas.dataset.values) : [];

    const ctx = canvas.getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: dataValues,
                backgroundColor: [
                    '#3498db', '#2ecc71', '#e74c3c', '#f1c40f', '#9b59b6', '#1abc9c'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, // 高さがはみ出るのを防ぐ設定
            plugins: {
                legend: { position: 'bottom' },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const value = context.raw;
                            // 全カテゴリの合計値を計算
                            const total = context.dataset.data.reduce((sum, val) => sum + val, 0);
                            
                            // 割合（%）を計算
                            const percentage = total > 0 ? Math.floor((value / total) * 100) : 0;
                            
                            return `${context.label}: ${value}h (${percentage}%)`;
                        }
                    }
                }
            }
        }
    });
});