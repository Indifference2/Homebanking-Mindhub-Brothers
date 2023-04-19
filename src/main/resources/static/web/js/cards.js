const {createApp} = Vue;

createApp({
    data(){
        return{
            dataCards: [],
            cardsDebit: [],
            cardsCredit: [],
            display: 1022,
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
            axios.get("http://localhost:8080/api/clients/1")
            .then((response)=> {
                this.dataCards = response.data.cards
                console.log(this.dataCards)
                this.filterTypeCard(this.dataCards)
            })
            .catch(error => console.log(error))
            },
        onResize(){
            this.display = windows.innerWidth
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