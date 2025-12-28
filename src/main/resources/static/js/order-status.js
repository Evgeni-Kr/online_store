async function updateStatus() {
    const orderId = document.getElementById("orderId").value;
    const status = document.getElementById("status").value;

    if (!orderId) {
        showMessage("Введите ID заказа", false);
        return;
    }

    try {
        const response = await fetch(`/api/order/${orderId}/status`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ status })
        });

        const data = await response.text();
        if (response.ok) {
            showMessage("Статус успешно обновлен ✔", true);
        } else {
            showMessage("Ошибка: " + data, false);
        }
    } catch (err) {
        showMessage("Ошибка соединения с сервером", false);
    }
}

function showMessage(msg, success) {
    const block = document.getElementById("response");
    block.style.display = "block";
    block.className = "response " + (success ? "success" : "error");
    block.textContent = msg;
}
