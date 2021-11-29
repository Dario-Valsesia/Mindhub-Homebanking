const app = Vue.createApp({
    data(){
        return{
            darkMode:false,
            client:[],
            cards:[],
            creditCards:[],
            debitCards:[],
            viewCards:false,
            viewModal:false,
            numberCard:'',
            errorMessage:'',
            requestMessage:'',
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
                    this.viewCards=true;
                })
                .catch(error=>error);
        },
        setDarkMode(){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode)); 
            document.querySelector('.btn-nav-close').click();      
        },
        rotateCard(e){
              console.log(e.target.type=="submit");
              if(e.target.type=="submit"){
                this.numberCard=e.target.value;
                this.viewModal=true;
              }
              e.target.closest('.card').classList.toggle('toTurnJS');
        },
        singOut(){
            axios.post('/api/logout').then(response => console.log(response))
        },

        deleteCard(){
            axios.delete(`/api/cards/delete?number=${this.numberCard}`).then(res=>{
                this.requestMessage="Deleted successfully";
                setTimeout(()=>location.reload(),1200)             
            }).catch(e=>this.errorMessage=e.response.data)
        },
        closeModal(){   
            this.viewModal=false;
            location.reload();
        },
    }


})

let mount = app.mount('#app');