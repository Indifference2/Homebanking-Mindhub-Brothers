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
            axios.get("http://localhost:8080/api/clients/1")
            .then((response)=> {
                this.dataCards = response.data.cards
                console.log(this.dataCards)
                this.filterTypeCard(this.dataCards)
            })
            .catch(error => console.log(error))
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