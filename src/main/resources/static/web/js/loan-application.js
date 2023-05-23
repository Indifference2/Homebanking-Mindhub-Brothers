const {createApp} = Vue

createApp({
    data(){
        return{
            dataLoans : [],
            dataAccounts : [],
            loanId : "",
            filterLoan : "",
            loanSelectPayments : [],
            payment : "",
            amount : "",
            accountNumber : "",
            loanInterest : "",
            roleUser: "",
        }
    },
    created(){
        this.getData()
        this.getAccounts()
        this.getRole()
    },
    mounted(){
    },
    methods:{
        getAccounts(){
            axios.get("http://localhost:8080/api/clients/current/accounts")
            .then(response =>{
                this.dataAccounts = response.data
            })
            .catch(error =>{
                console.log(error)
            })
        },
        getRole(){
            axios.get("/api/clients/current/rol")
            .then(response =>{
                this.roleUser = response.data
            })
        },
        getData(){
            axios.get("http://localhost:8080/api/loans")
            .then(response =>{
                this.dataLoans = response.data
                this.filterByLoanId(this.loanId)
                this.loanPayments(this.filterLoan)
                })
        },
        selectLoan(e){
            this.loanId = e
            this.getData()
        },
        filterByLoanId(idLoan){
            this.filterLoan = this.dataLoans.filter(loan => loan.id == idLoan)[0]
            this.loanInterest = this.filterLoan.interest
        },
        loanPayments(loan){
            this.loanSelectPayments = loan.payments.map(payment => payment)
        },
        createLoan(){
            Swal.fire({
                title: 'Are you sure you want confirm?',
                showCancelButton: true,
                color: "white",
                confirmButtonText: 'Yes',
                confirmButtonColor: "#009269",
                preConfirm: () =>{
                    return axios.post("http://localhost:8080/api/loans",{
                        id : this.loanId,
                        amount : this.amount,
                        payments : this.payment,
                        accountNumber : this.accountNumber,
                    })
                    .then(() =>{
                        let timerInterval;
                        Swal.fire({
                        icon: "success",
                        title: "Success",
                        html: `Loan approved succesfully, you will redirect automatically in <strong></strong>`,
                        timer: 5000,
                        showConfirmButton : false,
                        didOpen: () =>{
                            const content = Swal.getHtmlContainer()
                            timerInterval = setInterval(() =>{
                                Swal.getHtmlContainer().querySelector('strong')
                                    .textContent = (Swal.getTimerLeft() / 1000)
                                        .toFixed(0)
                            }, 100)
                        }
                        
                    }
                )
                    .then(() =>{
                        window.location.href="/web/pages/accounts.html"
                    })
                })
                .catch(error =>{
                    Swal.fire({
                        title: "There is a problem!",
                        text: error.response.data,
                        icon: "error",
                        confirmButtonColor : "#009269",
                    })
                })
                }
            })
        },
        logout(){
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                background : "url(../img/bg-alert.jpg)",
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor : "#009269",
                preConfirm: (login) => {
                    return axios.post('/api/logout')
                        .then(response => {
                            window.location.href="/web/index.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                'Request failed: ${error}'
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
    }
}).mount("#App")

window.onload = function () {
    $('#onload').fadeOut();
    $('body').removeClass('hidden');
};