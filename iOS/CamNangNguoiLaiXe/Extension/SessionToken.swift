//
//  SessionToken.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/4/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class SessionToken: NSObject {
    var globalData = [ModelData]()
    static let sharedInstance = SessionToken()
}

extension String {
    
    func encode() -> String {
        return self.addingPercentEncoding( withAllowedCharacters: .urlQueryAllowed)!
    }
    
    var html2AttributedString: NSAttributedString? {
        guard let data = data(using: .utf8) else { return nil }
        do {
            return try NSAttributedString(data: data, options: [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: String.Encoding.utf8.rawValue], documentAttributes: nil)
        } catch let error as NSError {
            print(error.localizedDescription)
            return  nil
        }
    }
    var html2String: String {
        return html2AttributedString?.string ?? ""
    }
}
