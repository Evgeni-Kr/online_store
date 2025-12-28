document.addEventListener("DOMContentLoaded", () => {

    const btn = document.getElementById("buy-now-btn");
    if (!btn) return;

    btn.addEventListener("click", async () => {
        const productId = document.getElementById("productId").value;
        const quantity = document.getElementById("quantity").value || 1;

        const csrf = document.querySelector('input[name="_csrf"]');

        const headers = {
            "Content-Type": "application/json"
        };

        if (csrf){
            headers["X-CSRF-TOKEN"] = csrf.value;
        }

        const response = await fetch(`/api/order/buy-now?productId=${productId}&quantity=${quantity}`, {
            method: "POST",
            headers: headers,
            credentials: "include"
        });

        if(response.ok){
            const order = await response.json();
            alert(`Заказ №${order.id} успешно создан!`);
            window.location.href = "/order/get";
        }
        else if(response.status === 401){
            alert("Авторизуйтесь для оформления заказа");
            window.location.href = "/login";
        }
        else {
            alert(await response.text());
        }
    });

});
