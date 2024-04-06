import {IAlbums} from "./Albums";
import {Image} from "./image";

export interface IAnime{
    banner: {
        color:string,
        image: Image
    },
    cardImagePath : string
    description : string | null
    folderName : string
    id : number
    albums : IAlbums[]
    releaseYear : string
    studio : string | null
    title : string
}