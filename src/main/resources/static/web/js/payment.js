const app = Vue.createApp({
    data(){
        return{
            darkMode:false,
            client:[],
            cards:[],
            accounts:[],
            //VIEW
            viewAccount:true,
            viewCard:false,
            viewAmountCvv:false,
            viewDescription:false,
            viewConfirm:false,
            viewModal:false,
            //SELECT 
            selectAccount:'',
            selectCard:'',
            selectAmount:'',
            selectCvv:'',
            selectDescription:'',
            requestMessage:'',
            errorMessage:'',

        }
    },
    created(){
        this.darkMode = JSON.parse(localStorage.getItem('dark'));
        this.loadData();
    },
    methods:{
        setDarkMode(e){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode));
            document.querySelector('.btn-nav-close').click();
        },
        loadData(){
            axios.get(`/api/clients/current`)
                .then(res=>{
                    this.client=res.data;
                    this.cards=res.data.cards;
                    this.accounts = res.data.accounts;

                })
                .catch(error=>console.log(error));
        },
        createPayment(){
            axios.post('/api/payments',{
                "number":this.selectCard,
                "cvv":this.selectCvv,
                "amount":this.selectAmount,
                "description":this.selectDescription,
                "numberAccount":this.selectAccount
            }).then(res=>{
                if(res.status==201){
                    this.requestMessage="Payments made correctly"
                }
                setTimeout(()=>{
                    location.reload();
                },2000)
            }).catch(e=>this.errorMessage=e.response.data)
        },
        //Methods view
        nextAccount(e){
            this.selectAccount=e.target.value;
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan');
            this.viewCard=true;
            setTimeout(()=>{
                this.viewAccount=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextCard(e){
            this.selectCard=e.target.value
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan')
            this.viewAmountCvv=true;
            setTimeout(()=>{
                this.viewCard=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextAmount(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan')
            this.viewDescription=true;
            setTimeout(()=>{
                this.viewAmountCvv=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextDescription(e){
            this.selectPayment=e.target.value;
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('exitLoan')
            this.viewConfirm=true;
            setTimeout(()=>{
                this.viewDescription=false;
                element.classList.remove('exitLoan');
            },1100)
        },
        nextConfirm(){
            this.viewModal=true;
        },
        closeModal(){   
            this.viewModal=false;
            location.reload();
        },
        //BACK METHODS
        backCard(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewAccount=true;
            setTimeout(()=>{
                this.viewCard=false;
                element.classList.remove('backLoan');
            },1100)
        },
        backAmount(e){
            this.selectAmount=0;
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewCard=true;
            setTimeout(()=>{
                this.viewAmountCvv=false;
                element.classList.remove('backLoan');
            },1100)
        },
        backDescription(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewAmountCvv=true;
            setTimeout(()=>{
                this.viewDescription=false;
                element.classList.remove('backLoan');
            },1100)
        },
        backConfirm(e){
            let element = e.target.closest('.containerOptionLoan');
            element.classList.add('backLoan')
            this.viewDescription=true;
            setTimeout(()=>{
                this.viewConfirm=false;
                element.classList.remove('backLoan');
            },1100)
        }
    }

})

let mount = app.mount('#app');