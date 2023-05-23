const { createApp } = Vue;

createApp({
    data() {
        return {
            dataCards: [],
            cardsDebit: [],
            cardsCredit: [],
            display: null,
            numberCardToDelete: "",
            currentDay : "",
            currentMonth : "",
            currentYear : "",
            cardExpiryMonth : "",
            cardExpiryYear : "",
            currentDate : "",
            roleUser: "",
        }
    },
    created() {
        this.loadData();
        this.getRole();
        const date = new Date();
        this.currentDay = date.getDate()
        this.currentMonth = date.getMonth() + 1
        this.currentYear = date.getFullYear()
        this.currentDate = this.currentYear + "-" + this.currentMonth + "-" + this.currentDay


    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current/cards")
                .then((response) => {
                    this.dataCards = response.data
                    console.log(this.dataCards)
                    this.filterTypeCard(this.dataCards)
                })
                .catch(error => console.log(error))
        },
        logout() {
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                background: "url(../img/bg-alert.jpg)",
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor: "#00845F",
                preConfirm: (login) => {
                    return axios.post('/api/logout')
                        .then(response => {
                            window.location.href = "/web/index.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                'Request failed: ${error.response.data}'
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        getRole(){
            axios.get("/api/clients/current/rol")
            .then(response =>{
                this.roleUser = response.data
            })
        },
        redirectAddCard() {
            window.location.href = "/web/pages/create-cards.html"
        },
        filterTypeCard(cards) {
            this.cardsDebit = cards.filter(card => card.cardType === "DEBIT")
            this.cardsCredit = cards.filter(card => card.cardType === "CREDIT")
        },
        deleteCard(numberCardToDelete) {
            Swal.fire({
                title: 'Are you sure that you want delete this card?',
                background: "url(../img/bg-alert.jpg)",
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor: "#00845F",
                preConfirm: (login) => {
                    return axios.put("http://localhost:8080/api/clients/current/cards?numberCard=" + numberCardToDelete)
                        .then((response) => {
                            Swal.fire({
                                icon: "success",
                                text: "Card deleted successfully"
                            })
                                .then((response) => {
                                    window.location.href = "/web/pages/cards.html"
                                })
                                .catch((error) => {
                                    Swal.fire({
                                        icon: "error",
                                        text: error.response.data,
                                    })
                                })
                        })
                }
            })
        },
        checkExpiry(date){
            if(date < this.currentDate){
                return true;
            }else{
                return false;
            }    
        },
    },


}).mount("#app")

window.onload = function () {
    $('#onload').fadeOut();
    $('body').removeClass('hidden');
};
