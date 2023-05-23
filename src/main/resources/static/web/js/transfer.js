const {createApp} = Vue 

createApp({
    data(){
        return{
            accountClient: [],
            accountOwnOrigin: "",
            accountOwnDestiny: "",
            accountThirdOrigin: "",
            accountThirdDestiny: "",
            activePageOwnAccounts: 1,
            activePageThirdPartyAccounts:1,
            fullNameClientThird: "",
            nameClientThird : "",
            lastNameClientThird : "",
            optionTransfer : true,
            amount : null,
            description: "",
            descriptionThird : "",
            amountThird : null,
            num: 0,
            roleUser : "",
        }
    },
    created(){
        this.loadData()
        this.getRole()
    },
    mounted(){
        console.log(this.$refs)
    },
    methods:{
        loadData(){
            axios.get('http://localhost:8080/api/clients/current/accounts')
            .then(response =>{
                this.accountClient = response.data
            })
        },
        getRole(){
            axios.get("/api/clients/current/rol")
            .then(response =>{
                this.roleUser = response.data
            })
        },
        updateCustomValidity(){
            let inputRef = this.$refs.input
            inputRef.oninvalid = inputRef.setCustomValidity("Number account not found")
            
        },
        loadDataClientThird(){
            axios.get('http://localhost:8080/api/accounts/clients/name',{
                params:{
                    numberAccount : "VIN-" + this.accountThirdDestiny
                }
            })
            .then(response =>{
                this.fullNameClientThird = response.data.split(" ")
                this.nameClientThird = this.fullNameClientThird[0]
                this.lastNameClientThird = this.fullNameClientThird[1]
                this.activePageThirdPartyAccounts = 2
            })
            .catch(error =>{
                Swal.fire({
                    title: "There was a problem!",
                    icon: "error",
                    text: "Account destiny not found"
                })
            })
        },
        makeTransferOwn(){
            Swal.fire({
                title: 'Are you sure you want to confirm the transfer?',
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor : "#009269",
                preConfirm: (login) => {
                    return axios.post('http://localhost:8080/api/transactions',{
                        balance : this.amount,
                        description : this.description,
                        numberAccountOrigin : this.accountOwnOrigin,
                        numberAccountDestiny : this.accountOwnDestiny,
                    },{
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    })
                    .then(response => {
                        Swal.fire({
                            title: 'Success',
                            text : response.data,
                            icon: "success",
                            timer : 2000,
                            showConfirmButton: false,
                            })
                        .then(()=>{
                            window.location.href="/web/pages/transfers.html"
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
        },
        makeTransferThird(){
            Swal.fire({
                title: 'Are you sure you want to confirm the transfer?',
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor : "#009269",
                preConfirm: (login) => {
                    return axios.post('http://localhost:8080/api/transactions',{
                        balance : this.amountThird,
                        description : this.descriptionThird,
                        numberAccountOrigin : this.accountThirdOrigin,
                        numberAccountDestiny : "VIN-" +  this.accountThirdDestiny,
                    },{
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    })
                    .then(response => {
                        Swal.fire({
                            title: 'Success',
                            text : response.data,
                            icon: "success",
                            timer : 2000,
                            showConfirmButton: false,
                            })
                        .then(()=>{
                            window.location.href="/web/pages/transfers.html"
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
        },
        logout(){
            Swal.fire({
                title: 'Are you sure that you want to log out?',
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
})
.mount("#app")

window.onload = function () {
    $('#onload').fadeOut();
    $('body').removeClass('hidden');
};
