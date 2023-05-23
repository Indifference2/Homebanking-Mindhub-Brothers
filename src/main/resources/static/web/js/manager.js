const { createApp } = Vue

createApp({
    data() {
        return {
            data: [],
            firstName: null,
            lastName: null,
            email: null,
            dataLoans: [],
            nameLoan: "",
            maxAmount: "",
            paymentsLoan: "",
            interestLoan: "",
        }
    },
    created() {
        this.loadLoans()
    },
    methods: {
        loadLoans() {
            axios.get("/api/loans")
                .then(response => {
                    this.dataLoans = response.data
                })
        },
        createLoan() {
            Swal.fire({
                title: 'Are you sure you want to confirm the transfer?',
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor: "#009269",
                preConfirm: () => {
                    return axios.post("/api/manager/loans", {
                        name: this.nameLoan,
                        maxAmount: this.maxAmount,
                        payments: this.paymentsLoan.split(','),
                        interest: this.interestLoan,
                    })
                        .then(response => {
                            Swal.fire({
                                title: 'Success',
                                text: response.data,
                                icon: "success",
                                timer: 2000,
                                showConfirmButton: false,
                            })
                                .then(() => {
                                    window.location.href = "/web/pages/manager.html"
                                })
                        })
                        .catch(error => {
                            Swal.fire({
                                title: "There was a problem!",
                                icon: "error",
                                text: error.response.data,
                            })
                        })
                }
            })
        }
    },
}).mount('#app')

window.onload = function () {
    $('#onload').fadeOut();
    $('body').removeClass('hidden');
};