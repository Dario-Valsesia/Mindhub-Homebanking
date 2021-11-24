const app = Vue.createApp({
    data(){
        return{
            darkMode:false,
            client:[],
            cards:[],
            creditCards:[],
            debitCards:[],
        }
    },
    created(){
        this.loadData();
        this.darkMode = JSON.parse(localStorage.getItem('dark'));
    },
    methods:{
        loadData(){
            axios.get(`/api/clients/current`)
                .then(res=>{
                    this.client=res.data;
                    this.cards=res.data.cards
                    this.creditCards=res.data.cards.filter(card=>card.cardType == "CREDIT");
                    this.debitCards=res.data.cards.filter(card=>card.cardType == "DEBIT");
                })
                .catch(error=>error);
        },
        setDarkMode(){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode)); 
            document.querySelector('.btn-nav-close').click();      
        },
        rotateCard(e){
              e.target.closest('.card').classList.toggle('toTurnJS');
        },
        singOut(){
            axios.post('/api/logout').then(response => console.log(response))
        }
    }


})

let mount = app.mount('#app');