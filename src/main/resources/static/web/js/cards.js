const {createApp} = Vue;

createApp({
    data(){
        return{
            dataCards: [],
            cardsDebit: [],
            cardsCredit: [],
            display: null,
        }
    },
    created(){
        this.loadData();
        window.addEventListener('resize', this.onResize)
    },
    beforeUnmount() {
        window.removeEventListener('resize', this.onResize)
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then((response)=> {
                this.dataCards = response.data.cards
                console.log(this.dataCards)
                this.filterTypeCard(this.dataCards)
            })
            .catch(error => console.log(error))
            },
        logout(){
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                background : "url(../img/bg-alert.jpg)",
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor : "#00845F",
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
        onResize(){
            if (window.innerWidth > 1024){
                this.display = true
            }else{
                this.display = false
            }
        },
        filterTypeCard(cards){
            this.cardsDebit = cards.filter(card => card.cardType === "DEBIT")
            this.cardsCredit = cards.filter(card =>card.cardType === "CREDIT")
        }
    },
    

}).mount("#app")

        const swiper = new Swiper(".mySwiper", {
            pagination: {
                el: ".swiper-pagination",
                dynamicBullets: true,
                type: 'bullets',
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
        });