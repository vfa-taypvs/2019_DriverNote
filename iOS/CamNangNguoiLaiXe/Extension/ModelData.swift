//
//  ModelData.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/4/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit
import ObjectMapper

class content: Mappable {
    @objc dynamic var id = ""
    dynamic var post_id = ""
    dynamic var title = ""
    dynamic var detail = ""
    dynamic var image = ""
    dynamic var position = ""
    
    required convenience init?(map: Map) {
        self.init()
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        post_id <- map["post_id"]
        title <- map["title"]
        detail <- map["detail"]
        image <- map["image"]
        position <- map["position"]
    }
}

class smallTopic: Mappable {
    dynamic var id = ""
    dynamic var title = ""
    dynamic var category_id = ""
    dynamic var type_id = ""
    dynamic var position = ""
    dynamic var type_name = ""
    var content: [content]?
    
    required convenience init?(map: Map) {
        self.init()
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        title <- map["title"]
        category_id <- map["category_id"]
        type_id <- map["type_id"]
        position <- map["position"]
        type_name <- map["type_name"]
        content <- map["content"]
    }
}

class ModelData: Mappable {
    dynamic var id = ""
    dynamic var name = ""
    dynamic var parent_id = ""
    dynamic var type_id = ""
    dynamic var version = ""
    dynamic var icon = ""
    dynamic var position = ""
    dynamic var count = ""
    dynamic var type_name = ""
    var small_topic: [smallTopic]?
    
    required convenience init?(map: Map) {
        self.init()
        
    }
    
    func setName (nameSet: String) {
        self.name = nameSet
    }
    
    func setType (typeSet: String) {
        self.type_name = typeSet
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        name <- map["name"]
        parent_id <- map["parent_id"]
        type_id <- map["type_id"]
        version <- map["version"]
        icon <- map["icon"]
        position <- map["position"]
        count <- map["count"]
        type_name <- map["type_name"]
        small_topic <- map["small_topic"]
    }
}
